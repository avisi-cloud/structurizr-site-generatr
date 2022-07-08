package nl.avisi.structurizr.site.generatr.site

import com.structurizr.Workspace
import kotlinx.html.*
import kotlinx.html.stream.appendHTML
import nl.avisi.structurizr.site.generatr.homeSection
import nl.avisi.structurizr.site.generatr.internalSoftwareSystems
import nl.avisi.structurizr.site.generatr.site.context.*
import nl.avisi.structurizr.site.generatr.site.model.HomePageViewModel
import nl.avisi.structurizr.site.generatr.site.model.PageViewModel
import nl.avisi.structurizr.site.generatr.site.model.WorkspaceDecisionsPageViewModel
import nl.avisi.structurizr.site.generatr.site.model.WorkspaceDocumentationSectionPageViewModel
import nl.avisi.structurizr.site.generatr.site.pages.*
import nl.avisi.structurizr.site.generatr.site.pages.softwaresystem.softwareSystemDecisionPage
import nl.avisi.structurizr.site.generatr.site.pages.softwaresystem.softwareSystemPage
import nl.avisi.structurizr.site.generatr.site.views.homePage
import nl.avisi.structurizr.site.generatr.site.views.workspaceDecisionsPage
import nl.avisi.structurizr.site.generatr.site.views.workspaceDocumentationSectionPage
import java.io.File
import java.nio.file.Path

fun copySiteWideAssets(exportDir: File) {
    val css = object {}.javaClass.getResource("/assets/css/style.css")?.readText()
        ?: throw IllegalStateException("CSS file not found on classpath")
    val cssDir = File(exportDir, "css").apply { mkdirs() }
    val cssFile = File(cssDir, "style.css")

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
    val contexts = sequence {
        context.workspace.model.internalSoftwareSystems.forEach { softwareSystem ->
            yield(SoftwareSystemInfoPageContext(context, softwareSystem))
            yield(SoftwareSystemContextPageContext(context, softwareSystem))
            yield(SoftwareSystemContainerPageContext(context, softwareSystem))
            yield(SoftwareSystemComponentPageContext(context, softwareSystem))
            yield(SoftwareSystemDeploymentPageContext(context, softwareSystem))
            yield(SoftwareSystemDecisionsPageContext(context, softwareSystem))
            yield(SoftwareSystemDependenciesPageContext(context, softwareSystem))

            softwareSystem.documentation.decisions.forEach {
                yield(SoftwareSystemDecisionPageContext(context, softwareSystem, it))
            }
        }
        context.workspace.documentation.sections
            .filter { it != context.workspace.documentation.homeSection }
            .forEach { yield(DocumentationSectionPageContext(context, it)) }
        context.workspace.documentation.decisions
            .forEach { yield(WorkspaceDecisionPageContext(context, it)) }
        yield(SoftwareSystemsOverviewPageContext(context))
    }
    contexts.forEach { writeHtmlFile(File(exportDir, context.currentBranch), it) }

    val branchDir = File(exportDir, context.currentBranch)
    writeHtmlFile(branchDir, HomePageViewModel(context))
    writeHtmlFile(branchDir, WorkspaceDecisionsPageViewModel(context))

    context.workspace.documentation.sections
        .filter { it.order != 1 }
        .forEach { writeHtmlFile(branchDir, WorkspaceDocumentationSectionPageViewModel(context, it)) }
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
                    is WorkspaceDecisionsPageViewModel -> workspaceDecisionsPage(viewModel)
                    is WorkspaceDocumentationSectionPageViewModel -> workspaceDocumentationSectionPage(viewModel)
                }
            }
        }
    )
}

fun writeHtmlFile(exportDir: File, context: AbstractPageContext) {
    val htmlFile = File(exportDir, context.htmlFile)
    htmlFile.parentFile.mkdirs()
    htmlFile.writeText(
        buildString {
            appendLine("<!doctype html>")
            appendHTML().html {
                attributes["class"] = "has-background-light"
                when (context) {
                    is SoftwareSystemDecisionPageContext -> softwareSystemDecisionPage(context)
                    is AbstractSoftwareSystemPageContext -> softwareSystemPage(context)
                    is SoftwareSystemsOverviewPageContext -> softwareSystemsOverviewPage(context)
                    is WorkspaceDecisionPageContext -> workspaceDecisionPage(context)
                }
            }
        }
    )
}
