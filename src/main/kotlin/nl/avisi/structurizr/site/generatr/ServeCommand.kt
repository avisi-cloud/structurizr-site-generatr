@file:Suppress("EXPERIMENTAL_IS_NOT_ENABLED")
@file:OptIn(ExperimentalCli::class)

package nl.avisi.structurizr.site.generatr

import com.sun.nio.file.SensitivityWatchEventModifier
import jakarta.servlet.ServletContext
import kotlinx.cli.*
import nl.avisi.structurizr.site.generatr.site.copySiteWideAssets
import nl.avisi.structurizr.site.generatr.site.generateDiagrams
import nl.avisi.structurizr.site.generatr.site.generateRedirectingIndexPage
import nl.avisi.structurizr.site.generatr.site.generateSite
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.DefaultServlet
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlet.ServletHolder
import org.eclipse.jetty.websocket.api.Session
import org.eclipse.jetty.websocket.api.WebSocketAdapter
import org.eclipse.jetty.websocket.server.JettyWebSocketServerContainer
import org.eclipse.jetty.websocket.server.config.JettyWebSocketServletContainerInitializer
import java.io.File
import java.nio.file.*
import java.time.Duration
import kotlin.io.path.extension
import kotlin.io.path.isDirectory
import kotlin.io.path.isRegularFile

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
        val exportDir = File(siteDir, branch)

        try {
            broadcast("site-updating")
            val workspace = createStructurizrWorkspace(File(workspaceFile))
            println("Generating diagrams...")
            generateDiagrams(workspace, exportDir)

            println("Generating site...")
            copySiteWideAssets(File(siteDir))
            generateRedirectingIndexPage(File(siteDir), branch)
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
            println("Successfully generated diagrams and site")
        } catch (e: Exception) {
            updateSiteError = e.message ?: "Unknown error"
            broadcast(updateSiteError!!)
            e.printStackTrace()
        }
    }

    private fun runServer(): Server =
        Server(8080).also { server ->
            println("Starting server...")

            server.handler = createServletContextHandler()
            server.start()

            println("Server started")
            println("Open http://localhost:8080 in your browser to view the site")
        }

    private fun createServletContextHandler() =
        ServletContextHandler().apply {
            contextPath = "/"
            addServlet(createStaticResourceServlet(), "/*")
            addWebSocketServlet(this, "/_events")
        }

    private fun createStaticResourceServlet() =
        ServletHolder("default", DefaultServlet()).apply {
            setInitParameter("resourceBase", siteDir)
        }

    private fun addWebSocketServlet(
        context: ServletContextHandler,
        @Suppress("SameParameterValue") pathSpec: String
    ) =
        JettyWebSocketServletContainerInitializer
            .configure(context) { _: ServletContext?, container: JettyWebSocketServerContainer ->
                container.idleTimeout = Duration.ZERO
                container.addMapping(pathSpec) { _, _ -> EventSocket() }
            }

    private fun startWatchService(): WatchService {
        val path = File(workspaceFile).absoluteFile.parentFile.toPath()
        val watchService = FileSystems.getDefault().newWatchService()

        path.watch(watchService)
        Files.walk(path)
            .filter { it.isDirectory() }
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
                .filter { it.isDirectory() }
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

    private inner class EventSocket : WebSocketAdapter() {
        override fun onWebSocketConnect(sess: Session?) {
            super.onWebSocketConnect(sess)
            synchronized(eventSocketsLock) { eventSockets.add(this) }
            updateSiteError?.let { send(it) }
        }

        override fun onWebSocketClose(statusCode: Int, reason: String?) {
            synchronized(eventSocketsLock) { eventSockets.remove(this) }
        }

        override fun onWebSocketError(cause: Throwable?) {
            synchronized(eventSocketsLock) { eventSockets.remove(this) }
        }

        fun send(message: String) {
            if (session != null && session.isOpen)
                this.session.remote?.sendString(message)
        }
    }
}
