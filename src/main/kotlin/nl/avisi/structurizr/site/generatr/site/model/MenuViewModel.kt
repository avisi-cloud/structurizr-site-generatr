package nl.avisi.structurizr.site.generatr.site.model

import nl.avisi.structurizr.site.generatr.includedSoftwareSystems
import nl.avisi.structurizr.site.generatr.site.GeneratorContext

class MenuViewModel(generatorContext: GeneratorContext, private val pageViewModel: PageViewModel) {
    val generalItems = sequence {
        yield(createMenuItem("Home", HomePageViewModel.url()))

        if (generatorContext.workspace.documentation.decisions.isNotEmpty())
            yield(createMenuItem("Decisions", WorkspaceDecisionsPageViewModel.url(), false))

        if (generatorContext.workspace.model.softwareSystems.isNotEmpty())
            yield(createMenuItem("Software Systems", SoftwareSystemsPageViewModel.url()))

        generatorContext.workspace.documentation.sections
            .sortedBy { it.order }
            .drop(1)
            .forEach { yield(createMenuItem(it.contentTitle(), WorkspaceDocumentationSectionPageViewModel.url(it))) }
    }.toList()

    val softwareSystemItems = generatorContext.workspace.model.includedSoftwareSystems
        .sortedBy { it.name.lowercase() }
        .map {
            createMenuItem(it.name, SoftwareSystemPageViewModel.url(it, SoftwareSystemPageViewModel.Tab.HOME), false)
        }

    private val groupSeparator = generatorContext.workspace.model.properties["structurizr.groupSeparator"] ?: "/"

    private val softwareSystemPaths = generatorContext.workspace.model.includedSoftwareSystems
        .filter { it.group != null }
        .map { it.group + groupSeparator + it.name }
        .sortedBy { it.lowercase() }

    private fun createMenuItem(title: String, href: String, exact: Boolean = true) =
        LinkViewModel(pageViewModel, title, href, exact)

    fun softwareSystemNodes(): MenuNodeViewModel {
        data class MutableMenuNode(val name: String, val children: MutableList<MutableMenuNode>) {
            fun toMenuNode(): MenuNodeViewModel = MenuNodeViewModel(name, children.map { it.toMenuNode() })
        }

        val rootNode = MutableMenuNode("", mutableListOf())

        softwareSystemPaths.forEach { path ->
            var currentNode = rootNode
            path.split(groupSeparator).forEach { part ->
                val existingNode = currentNode.children.find { it.name == part }
                currentNode = if (existingNode == null) {
                    val newNode = MutableMenuNode(part, mutableListOf())
                    currentNode.children.add(newNode)
                    newNode
                } else {
                    existingNode
                }
            }
        }

        return rootNode.toMenuNode()
    }
}
