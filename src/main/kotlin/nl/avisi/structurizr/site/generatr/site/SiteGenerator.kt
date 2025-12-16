package nl.avisi.structurizr.site.generatr.site

import com.structurizr.Workspace
import com.structurizr.util.WorkspaceUtils
import kotlinx.html.*
import kotlinx.html.stream.appendHTML
import nl.avisi.structurizr.site.generatr.*
import nl.avisi.structurizr.site.generatr.site.model.*
import nl.avisi.structurizr.site.generatr.site.views.*
import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import java.util.concurrent.ConcurrentHashMap

fun copySiteWideAssets(exportDir: File) {
    copySiteWideAsset(exportDir, "/css/style.css")
    copySiteWideAsset(exportDir, "/js/header.js")
    copySiteWideAsset(exportDir, "/js/svg-modal.js")
    copySiteWideAsset(exportDir, "/js/modal.js")
    copySiteWideAsset(exportDir, "/js/search.js")
    copySiteWideAsset(exportDir, "/js/auto-reload.js")
    copySiteWideAsset(exportDir, "/css/admonition.css")
    copySiteWideAsset(exportDir, "/js/admonition.js")
    copySiteWideAsset(exportDir, "/js/reformat-mermaid.js")
    copySiteWideAsset(exportDir, "/css/treeview.css")
    copySiteWideAsset(exportDir, "/js/treeview.js")
    copySiteWideAsset(exportDir, "/js/katex-render.js")
    copySiteWideAsset(exportDir, "/js/toggle-theme.js")
}

private fun copySiteWideAsset(exportDir: File, asset: String) {
    val content = object {}.javaClass.getResource("/assets$asset")?.readText()
        ?: throw IllegalStateException("File $asset not found on classpath")
    val file = File(exportDir, asset.substringAfterLast('/'))

    file.writeText(content)
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
                    title { +"Structurizr site generatr" }
                }
                body()
            }
        }
    )
}

fun writeStructurizrJson(workspace: Workspace, exportDir: File) {
    val json = WorkspaceUtils.toJson(workspace, true)
    File(exportDir, "workspace.json")
        .apply { parentFile.mkdirs() }
        .writeText(json)
}

fun generateSite(
    version: String,
    workspace: Workspace,
    assetsDir: File?,
    exportDir: File,
    branches: List<String>,
    currentBranch: String,
    serving: Boolean = false
) {
    val generatorContext = GeneratorContext(version, workspace, branches, currentBranch, serving) { key, url ->
        val diagramCache = ConcurrentHashMap<String, Pair<String, String>>()
        workspace.views.views.singleOrNull { view -> view.key == key }
            ?.let { generateDiagramWithElementLinks(workspace, it, url, diagramCache) }
    }

    val branchDir = File(exportDir, currentBranch)

    deleteOldHashes(branchDir)
    if (assetsDir != null) copyAssets(assetsDir, branchDir)
    generateStyle(generatorContext, branchDir)
    generateHtmlFiles(generatorContext, branchDir)
}

private fun deleteOldHashes(branchDir: File) = branchDir.walk().filter { it.extension == "md5" }
    .forEach { it.delete() }

private fun copyAssets(assetsDir: File, branchDir: File) {
    assetsDir.copyRecursively(branchDir, overwrite = true)
}

private fun generateStyle(context: GeneratorContext, branchDir: File) {
    val configuration = context.workspace.views.configuration.properties
    val primary = configuration.getOrDefault("generatr.style.colors.primary", "#333333")
    val secondary = configuration.getOrDefault("generatr.style.colors.secondary", "#cccccc")

    val file = File(branchDir, "style-branding.css")
    val content = """
        .navbar .has-site-branding {
            background-color: $primary!important;
            color: $secondary!important;
        }
        .navbar .has-site-branding::after {
            border-color: $secondary!important;
        }
        .menu .has-site-branding a.is-active {
            color: $secondary!important;
            background-color: $primary!important;
        }
        .input.has-site-branding:focus {
            border-color: $secondary!important;
            box-shadow: 0 0 0 0.125em $secondary;
        }
    """.trimIndent()

    file.writeText(content)
}

private fun generateHtmlFiles(context: GeneratorContext, branchDir: File) {
    buildList {
        add { writeHtmlFile(branchDir, HomePageViewModel(context)) }
        add { writeHtmlFile(branchDir, WorkspaceDecisionsPageViewModel(context)) }
        add { writeHtmlFile(branchDir, SoftwareSystemsPageViewModel(context)) }
        add { writeHtmlFile(branchDir, SearchViewModel(context)) }

        context.workspace.documentation.sections
            .filter { it.order != 1 }
            .forEach {
                add { writeHtmlFile(branchDir, WorkspaceDocumentationSectionPageViewModel(context, it)) }
            }
        context.workspace.documentation.decisions
            .forEach {
                add { writeHtmlFile(branchDir, WorkspaceDecisionPageViewModel(context, it)) }
            }

        context.workspace.includedSoftwareSystems.forEach {
            add { writeHtmlFile(branchDir, SoftwareSystemHomePageViewModel(context, it)) }
            add { writeHtmlFile(branchDir, SoftwareSystemContextPageViewModel(context, it)) }
            add { writeHtmlFile(branchDir, SoftwareSystemContainerPageViewModel(context, it)) }
            add { writeHtmlFile(branchDir, SoftwareSystemDynamicPageViewModel(context, it)) }
            add { writeHtmlFile(branchDir, SoftwareSystemDeploymentPageViewModel(context, it)) }
            add { writeHtmlFile(branchDir, SoftwareSystemDependenciesPageViewModel(context, it)) }
            add { writeHtmlFile(branchDir, SoftwareSystemDecisionsPageViewModel(context, it)) }
            add { writeHtmlFile(branchDir, SoftwareSystemSectionsPageViewModel(context, it)) }

            it.documentation.decisions.forEach { decision ->
                add { writeHtmlFile(branchDir, SoftwareSystemDecisionPageViewModel(context, it, decision)) }
            }

            it.containers
                .filter { container -> container.hasDecisions(recursive = true) }
                .onEach { container ->
                    add { writeHtmlFile(branchDir, SoftwareSystemContainerDecisionsPageViewModel(context, container)) }
                    container.documentation.decisions.forEach { decision ->
                        add { writeHtmlFile(branchDir, SoftwareSystemContainerDecisionPageViewModel(context, container, decision)) }
                    }
                }
                .flatMap { container -> container.components }
                .filter { component -> component.hasDecisions() }
                .forEach { component ->
                    add { writeHtmlFile(branchDir, SoftwareSystemContainerComponentDecisionsPageViewModel(context, component)) }
                    component.documentation.decisions.forEach { decision ->
                        add { writeHtmlFile(branchDir, SoftwareSystemContainerComponentDecisionPageViewModel(context, component, decision)) }
                    }
                }

            it.containers
                .filter { container ->
                    context.workspace.hasComponentDiagrams(container) or
                            container.includedProperties.isNotEmpty() or
                            context.workspace.hasImageViews(container.id) }
                .forEach { container ->
                    add { writeHtmlFile(branchDir, SoftwareSystemContainerComponentsPageViewModel(context, container)) } }

            it.containers
                .filter { container ->
                    ( context.workspace.hasComponentDiagrams(container) or
                            context.workspace.hasImageViews(container.id)) }
                .forEach { container ->
                    container.components.filter { component ->
                        context.workspace.hasImageViews(component.id) }
                    .forEach { component ->
                        add { writeHtmlFile(branchDir, SoftwareSystemContainerComponentCodePageViewModel(context, container, component)) }
                    }
                }

            it.documentation.sections
                .filter { section -> section.order != 1 }
                .forEach { section ->
                    add { writeHtmlFile(branchDir, SoftwareSystemSectionPageViewModel(context, it, section)) }
                }

            it.containers
                .filter { container -> container.hasSections(recursive = true) }
                .onEach { container ->
                    add { writeHtmlFile(branchDir, SoftwareSystemContainerSectionsPageViewModel(context, container)) }
                    container.documentation.sections.forEach { section ->
                        add { writeHtmlFile(branchDir, SoftwareSystemContainerSectionPageViewModel(context, container, section)) }
                    }
                }
                .flatMap { container -> container.components }
                .filter { component -> component.hasSections() }
                .forEach { component ->
                    add { writeHtmlFile(branchDir, SoftwareSystemContainerComponentSectionsPageViewModel(context, component)) }
                    component.documentation.sections.forEach { section ->
                        add { writeHtmlFile(branchDir, SoftwareSystemContainerComponentSectionPageViewModel(context, component, section)) }
                    }
                }
        }
    }
        .parallelStream()
        .forEach { it.invoke() }
}

private fun writeHtmlFile(exportDir: File, viewModel: PageViewModel) {
    val html = buildString {
        appendLine("<!doctype html>")
        appendHTML().html {
            when (viewModel) {
                is HomePageViewModel -> homePage(viewModel)
                is SearchViewModel -> searchPage(viewModel)
                is SoftwareSystemsPageViewModel -> softwareSystemsPage(viewModel)
                is SoftwareSystemHomePageViewModel -> softwareSystemHomePage(viewModel)
                is SoftwareSystemContextPageViewModel -> softwareSystemContextPage(viewModel)
                is SoftwareSystemContainerPageViewModel -> softwareSystemContainerPage(viewModel)
                is SoftwareSystemContainerDecisionPageViewModel -> softwareSystemContainerDecisionPage(viewModel)
                is SoftwareSystemContainerDecisionsPageViewModel -> softwareSystemContainerDecisionsPage(viewModel)
                is SoftwareSystemContainerSectionPageViewModel -> softwareSystemContainerSectionPage(viewModel)
                is SoftwareSystemContainerSectionsPageViewModel -> softwareSystemContainerSectionsPage(viewModel)
                is SoftwareSystemContainerComponentsPageViewModel -> softwareSystemContainerComponentsPage(viewModel)
                is SoftwareSystemContainerComponentCodePageViewModel -> softwareSystemContainerComponentCodePage(viewModel)
                is SoftwareSystemContainerComponentDecisionPageViewModel -> softwareSystemContainerComponentDecisionPage(viewModel)
                is SoftwareSystemContainerComponentDecisionsPageViewModel -> softwareSystemContainerComponentDecisionsPage(viewModel)
                is SoftwareSystemContainerComponentSectionPageViewModel -> softwareSystemContainerComponentSectionPage(viewModel)
                is SoftwareSystemContainerComponentSectionsPageViewModel -> softwareSystemContainerComponentSectionsPage(viewModel)
                is SoftwareSystemDynamicPageViewModel -> softwareSystemDynamicPage(viewModel)
                is SoftwareSystemDeploymentPageViewModel -> softwareSystemDeploymentPage(viewModel)
                is SoftwareSystemDependenciesPageViewModel -> softwareSystemDependenciesPage(viewModel)
                is SoftwareSystemDecisionPageViewModel -> softwareSystemDecisionPage(viewModel)
                is SoftwareSystemDecisionsPageViewModel -> softwareSystemDecisionsPage(viewModel)
                is SoftwareSystemSectionPageViewModel -> softwareSystemSectionPage(viewModel)
                is SoftwareSystemSectionsPageViewModel -> softwareSystemSectionsPage(viewModel)
                is WorkspaceDecisionPageViewModel -> workspaceDecisionPage(viewModel)
                is WorkspaceDecisionsPageViewModel -> workspaceDecisionsPage(viewModel)
                is WorkspaceDocumentationSectionPageViewModel -> workspaceDocumentationSectionPage(viewModel)
            }
        }
    }

    val subDirectory = File(exportDir, viewModel.url)
    val htmlFile = File(subDirectory, "index.html")
    htmlFile.parentFile.mkdirs()
    htmlFile.writeText(html)

    val hash = MessageDigest.getInstance("MD5").digest(html.toByteArray())
        .let { BigInteger(1, it).toString(16) }

    val hashFile = File("${htmlFile.absolutePath}.md5")
    hashFile.writeText(hash)
}
