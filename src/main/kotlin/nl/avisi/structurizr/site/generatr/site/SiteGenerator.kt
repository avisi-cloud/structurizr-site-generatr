package nl.avisi.structurizr.site.generatr.site

import com.structurizr.Workspace
import kotlinx.html.*
import kotlinx.html.stream.appendHTML
import nl.avisi.structurizr.site.generatr.includedSoftwareSystems
import nl.avisi.structurizr.site.generatr.site.model.*
import nl.avisi.structurizr.site.generatr.site.views.*
import java.io.File
import java.nio.file.Path

fun copySiteWideAssets(exportDir: File) {
    val css = object {}.javaClass.getResource("/assets/css/style.css")?.readText()
        ?: throw IllegalStateException("CSS file not found on classpath")
    val cssFile = File(exportDir, "style.css")

    cssFile.writeText(css)
}

fun generateRedirectingIndexPage(exportDir: File, defaultBranch: String) {
    val htmlFile = File(exportDir, "index.html")
    htmlFile.writeText(
        buildString {
            appendLine("<!doctype html>")
            appendHTML().html {
                attributes["lang"] = "en"
                head {
                    meta {
                        httpEquiv = "refresh"
                        content = "0; url=$defaultBranch/"
                    }
                    title { +"PEC Architectuur" }
                }
                body()
            }
        }
    )
}

fun generateSite(
    version: String,
    workspace: Workspace,
    assetsDir: File?,
    exportDir: File,
    branches: List<String>,
    currentBranch: String
) {
    val generatorContext = GeneratorContext(version, workspace, branches, currentBranch)

    if (assetsDir != null) copyAssets(assetsDir, exportDir)
    generateHtmlFiles(generatorContext, exportDir)
}

private fun copyAssets(assetsDir: File, exportDir: File) {
    assetsDir.copyRecursively(exportDir, overwrite = true)
}

private fun generateHtmlFiles(context: GeneratorContext, exportDir: File) {
    val branchDir = File(exportDir, context.currentBranch)
    writeHtmlFile(branchDir, HomePageViewModel(context))
    writeHtmlFile(branchDir, WorkspaceDecisionsPageViewModel(context))
    writeHtmlFile(branchDir, SoftwareSystemsPageViewModel(context))

    context.workspace.documentation.sections
        .filter { it.order != 1 }
        .forEach { writeHtmlFile(branchDir, WorkspaceDocumentationSectionPageViewModel(context, it)) }
    context.workspace.documentation.decisions
        .forEach { writeHtmlFile(branchDir, WorkspaceDecisionPageViewModel(context, it)) }

    context.workspace.model.includedSoftwareSystems.forEach {
        writeHtmlFile(branchDir, SoftwareSystemHomePageViewModel(context, it))
        writeHtmlFile(branchDir, SoftwareSystemContextPageViewModel(context, it))
        writeHtmlFile(branchDir, SoftwareSystemContainerPageViewModel(context, it))
        writeHtmlFile(branchDir, SoftwareSystemComponentPageViewModel(context, it))
        writeHtmlFile(branchDir, SoftwareSystemDeploymentPageViewModel(context, it))
        writeHtmlFile(branchDir, SoftwareSystemDependenciesPageViewModel(context, it))
        writeHtmlFile(branchDir, SoftwareSystemDecisionsPageViewModel(context, it))

        it.documentation.decisions.forEach { decision ->
            writeHtmlFile(branchDir, SoftwareSystemDecisionPageViewModel(context, it, decision))
        }
    }
}

private fun writeHtmlFile(exportDir: File, viewModel: PageViewModel) {
    val htmlFile = File(exportDir, Path.of(viewModel.url, "index.html").toString())
    htmlFile.parentFile.mkdirs()
    htmlFile.writeText(
        buildString {
            appendLine("<!doctype html>")
            appendHTML().html {
                when (viewModel) {
                    is HomePageViewModel -> homePage(viewModel)
                    is SoftwareSystemsPageViewModel -> softwareSystemsPage(viewModel)
                    is SoftwareSystemHomePageViewModel -> softwareSystemHomePage(viewModel)
                    is SoftwareSystemContextPageViewModel -> softwareSystemContextPage(viewModel)
                    is SoftwareSystemContainerPageViewModel -> softwareSystemContainerPage(viewModel)
                    is SoftwareSystemComponentPageViewModel -> softwareSystemComponentPage(viewModel)
                    is SoftwareSystemDeploymentPageViewModel -> softwareSystemDeploymentPage(viewModel)
                    is SoftwareSystemDependenciesPageViewModel -> softwareSystemDependenciesPage(viewModel)
                    is SoftwareSystemDecisionPageViewModel -> softwareSystemDecisionPage(viewModel)
                    is SoftwareSystemDecisionsPageViewModel -> softwareSystemDecisionsPage(viewModel)
                    is WorkspaceDecisionPageViewModel -> workspaceDecisionPage(viewModel)
                    is WorkspaceDecisionsPageViewModel -> workspaceDecisionsPage(viewModel)
                    is WorkspaceDocumentationSectionPageViewModel -> workspaceDocumentationSectionPage(viewModel)
                }
            }
        }
    )
}
