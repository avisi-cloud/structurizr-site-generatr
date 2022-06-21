package nl.avisi.structurizr.site.generatr.site

import com.structurizr.Workspace
import kotlinx.html.*
import kotlinx.html.stream.appendHTML
import nl.avisi.structurizr.site.generatr.homeSection
import nl.avisi.structurizr.site.generatr.internalSoftwareSystems
import nl.avisi.structurizr.site.generatr.site.context.*
import nl.avisi.structurizr.site.generatr.site.context.SoftwareSystemsOverviewPageContext
import nl.avisi.structurizr.site.generatr.site.pages.documentationSectionPage
import nl.avisi.structurizr.site.generatr.site.pages.indexPage
import nl.avisi.structurizr.site.generatr.site.pages.softwareSystemsOverviewPage
import nl.avisi.structurizr.site.generatr.site.pages.softwaresystem.softwareSystemDecisionPage
import nl.avisi.structurizr.site.generatr.site.pages.softwaresystem.softwareSystemPage
import java.io.File

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
    contextPath: String,
    assetsDir: File?,
    exportDir: File,
    branches: List<String>,
    currentBranch: String
) {
    val generatorContext = GeneratorContext(version, workspace, contextPath, branches, currentBranch)

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
        yield(HomePageContext(context, context.workspace.documentation.homeSection))
        yield(SoftwareSystemsOverviewPageContext(context))
    }

    contexts.forEach { writeHtmlFile(exportDir, it) }
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
                    is HomePageContext -> indexPage(context)
                    is SoftwareSystemDecisionPageContext -> softwareSystemDecisionPage(context)
                    is AbstractSoftwareSystemPageContext -> softwareSystemPage(context)
                    is DocumentationSectionPageContext -> documentationSectionPage(context)
                    is SoftwareSystemsOverviewPageContext -> softwareSystemsOverviewPage(context)
                }
            }
        }
    )
}
