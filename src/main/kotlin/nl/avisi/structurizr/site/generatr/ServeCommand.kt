@file:OptIn(ExperimentalCli::class)

package nl.avisi.structurizr.site.generatr

import com.sun.nio.file.SensitivityWatchEventModifier
import kotlinx.cli.*
import nl.avisi.structurizr.site.generatr.site.*
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.handler.ContextHandler
import org.eclipse.jetty.server.handler.ContextHandlerCollection
import org.eclipse.jetty.server.handler.ResourceHandler
import org.eclipse.jetty.util.resource.ResourceFactory
import org.eclipse.jetty.websocket.api.Callback
import org.eclipse.jetty.websocket.api.Session
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketOpen
import org.eclipse.jetty.websocket.api.annotations.WebSocket
import org.eclipse.jetty.websocket.server.WebSocketUpgradeHandler
import java.io.File
import java.nio.file.*
import java.time.Duration
import kotlin.io.path.absolutePathString
import kotlin.io.path.extension
import kotlin.io.path.isDirectory
import kotlin.io.path.isHidden
import kotlin.io.path.isRegularFile
import kotlin.system.measureTimeMillis

class ServeCommand : Subcommand("serve", "Start a development server") {
    private val workspaceFile by option(
        ArgType.String, "workspace-file", "w", "The workspace file"
    ).required()
    private val assetsDir by option(
        ArgType.String, "assets-dir", "a", "Directory where static assets are located"
    )
    private val siteDir by option(
        ArgType.String, "site-dir", "s", "Directory for the generated site"
    ).default("build/serve")

    private val port by option(
        ArgType.Int, "port", "p", "Port the site is served on"
    ).default(8080)

    private val eventSockets = mutableListOf<EventSocket>()
    private val eventSocketsLock = Any()
    private var updateSiteError: String? = null

    override fun execute() {
        updateSite()
        val server = runServer()
        val watchService = startWatchService()

        try {
            server.join()
        } finally {
            println("Stop watching for changes")
            watchService.close()

            println("Stopping server...")
            server.stop()
            println("Server stopped")
        }
    }

    private fun updateSite() {
        val branch = "master"
        val exportDir = File(siteDir, branch).apply { mkdirs() }

        try {
            val ms = measureTimeMillis {
                broadcast("site-updating")
                val workspace = createStructurizrWorkspace(File(workspaceFile))

                println("Generating index page...")
                generateRedirectingIndexPage(File(siteDir), branch)
                println("Copying assets...")
                copySiteWideAssets(File(siteDir))
                println("Writing workspace.json...")
                writeStructurizrJson(workspace, exportDir)
                println("Generating diagrams...")
                generateDiagrams(workspace, exportDir)
                println("Generating site...")
                generateSite(
                    "0.0.0",
                    workspace,
                    assetsDir?.let { File(it) },
                    File(siteDir),
                    listOf(branch),
                    branch,
                    serving = true
                )

                updateSiteError = null
                broadcast("site-updated")
            }
            println("Successfully generated diagrams and site in ${ms.toDouble() / 1000} seconds")
        } catch (e: Exception) {
            updateSiteError = e.message ?: "Unknown error"
            broadcast(updateSiteError!!)
            e.printStackTrace()
        }
    }

    private fun runServer(): Server =
        Server(port).also { server ->
            println("Starting server...")

            server.handler = createRootContextHandler(server)
            server.start()

            println("Server started")
            println("Open http://localhost:$port in your browser to view the site")
        }

    private fun createRootContextHandler(server: Server) = ContextHandlerCollection(
        ContextHandler(createStaticResourceHandler(), "/"),
        ContextHandler("/").apply {
            handler = createWebSocketHandler(server, this, "/_events")
        }
    )

    private fun createStaticResourceHandler() =
        ResourceHandler().apply {
            baseResource = ResourceFactory.of(this).newResource(siteDir)
        }

    private fun createWebSocketHandler(
        server: Server,
        context: ContextHandler,
        @Suppress("SameParameterValue") pathSpec: String
    ) =
        WebSocketUpgradeHandler.from(server, context)
            .configure { container ->
                container.idleTimeout = Duration.ZERO
                container.addMapping(pathSpec) { _, _, _ -> EventSocket() }
            }

    private fun startWatchService(): WatchService {
        val path = File(workspaceFile).absoluteFile.parentFile.toPath()
        val watchService = FileSystems.getDefault().newWatchService()
        val absoluteSiteDir = File(siteDir).absolutePath

        path.watch(watchService)
        Files.walk(path)
            .filter { it.isDirectory() && !it.isHidden() && !it.absolutePathString().startsWith(absoluteSiteDir) }
            .forEach { it.watch(watchService) }

        Thread { monitorFileChanges(watchService) }
            .apply { isDaemon = true }
            .start()

        return watchService
    }

    private fun monitorFileChanges(watchService: WatchService) {
        while (true) {
            val watchKey = watchService.take()
            val parentPath = watchKey.watchable() as Path

            Thread.sleep(100) // Throttle the file watcher to give editors time for cleaning up temporary files
            val events = watchKey.pollEvents().filterNot { isHashFile(it) }

            val fileModified = events.map { parentPath.resolve(it.context() as Path) }
                .any { it.isRegularFile() }
            val fileOrDirectoryDeleted = events
                .any { it.kind() == StandardWatchEventKinds.ENTRY_DELETE }

            events.filter { it.kind() == StandardWatchEventKinds.ENTRY_CREATE }
                .map { parentPath.resolve(it.context() as Path) }
                .filter { it.isDirectory() && !it.isHidden() }
                .forEach { it.watch(watchService) }

            if (fileModified || fileOrDirectoryDeleted) {
                println("Detected a change in ${watchKey.watchable()}, updating site...")
                try {
                    updateSite()
                } catch (e: Exception) {
                    System.err.println("An error occurred while updating the site")
                    e.printStackTrace()
                }
            }
            watchKey.reset()
        }
    }

    private fun isHashFile(it: WatchEvent<*>) = (it.context() as Path).extension == "md5"

    private fun Path.watch(watchService: WatchService) {
        register(
            watchService,
            arrayOf(
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_DELETE,
                StandardWatchEventKinds.ENTRY_MODIFY
            ),
            SensitivityWatchEventModifier.HIGH
        )
    }

    private fun broadcast(message: String) = synchronized(eventSocketsLock) {
        eventSockets.forEach { it.send(message) }
    }

    @WebSocket
    inner class EventSocket {
        private var session: Session? = null

        @OnWebSocketOpen
        fun onWebSocketConnect(session: Session?) {
            synchronized(eventSocketsLock) { eventSockets.add(this) }
            this.session = session
            updateSiteError?.let { send(it) }
        }

        @OnWebSocketClose
        fun onWebSocketClose(statusCode: Int, reason: String?) {
            session = null
            synchronized(eventSocketsLock) { eventSockets.remove(this) }
        }

        @OnWebSocketError
        fun onWebSocketError(cause: Throwable?) {
            session = null
            synchronized(eventSocketsLock) { eventSockets.remove(this) }
        }

        fun send(message: String) {
            session?.let {
                if (it.isOpen)
                    it.sendText(message, Callback.NOOP)
            }
        }
    }
}
