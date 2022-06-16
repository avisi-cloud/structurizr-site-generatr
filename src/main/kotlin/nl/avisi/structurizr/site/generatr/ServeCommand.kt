@file:Suppress("EXPERIMENTAL_IS_NOT_ENABLED")
@file:OptIn(ExperimentalCli::class)

package nl.avisi.structurizr.site.generatr

import com.sun.nio.file.SensitivityWatchEventModifier
import kotlinx.cli.ArgType
import kotlinx.cli.ExperimentalCli
import kotlinx.cli.Subcommand
import kotlinx.cli.required
import nl.avisi.structurizr.site.generatr.site.copySiteWideAssets
import nl.avisi.structurizr.site.generatr.site.generateDiagrams
import nl.avisi.structurizr.site.generatr.site.generateRedirectingIndexPage
import nl.avisi.structurizr.site.generatr.site.generateSite
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.handler.ResourceHandler
import java.io.File
import java.nio.file.*
import kotlin.io.path.isDirectory
import kotlin.io.path.isRegularFile

class ServeCommand : Subcommand("serve", "Start a development server") {
    private val workspaceFile by option(
        ArgType.String, "workspace-file", "w", "The workspace file"
    ).required()
    private val assetsDir by option(
        ArgType.String, "assets-dir", "a", "Directory where static assets are located"
    ).required()
    private val siteDir by option(
        ArgType.String, "site-dir", "s", "Directory for the generated site"
    ).required()

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
        val workspace = parseStructurizrDslWorkspace(File(workspaceFile))
        val exportDir = File(siteDir, "master")

        println("Generating diagrams...")
        generateDiagrams(workspace, exportDir)

        println("Generating site...")
        copySiteWideAssets(File(siteDir))
        generateRedirectingIndexPage(File(siteDir), "master")
        generateSite("0.0.0", workspace, File(assetsDir), exportDir, listOf("master"), "master")

        println("Successfully generated diagrams and site")
    }

    private fun runServer(): Server {
        val server = Server(8080)
        val resourceHandler = ResourceHandler()

        resourceHandler.isDirAllowed = true
        resourceHandler.resourceBase = siteDir
        server.handler = resourceHandler

        println("Starting server...")
        server.start()
        println("Server started")
        println("Open http://localhost:8080 in your browser to view the site")

        return server
    }

    private fun startWatchService(): WatchService {
        val path = File(workspaceFile).parentFile.toPath()
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
            val events = watchKey.pollEvents()

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

    private fun Path.watch(watchService: WatchService) {
        register(
            watchService, arrayOf(
                StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE,
                StandardWatchEventKinds.ENTRY_MODIFY
            ),
            SensitivityWatchEventModifier.HIGH
        )
    }
}
