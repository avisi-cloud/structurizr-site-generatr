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

    private fun createMenuItem(title: String, href: String, exact: Boolean = true) =
        LinkViewModel(pageViewModel, title, href, exact)

    data class Node(val name: String, val children: MutableList<Node>)

    val nestedSoftwareSystems = generatorContext.workspace.model.includedSoftwareSystems
        .map {it.group + "/" + it.name}
        .sortedBy {it.lowercase()}

    fun buildTree(data: List<String>, delimiter: Char):MutableList<Node> {
        val rootNode = Node("", mutableListOf())
        for (path in data) {
            val parts = path.split(delimiter)
            var currentNode = rootNode

            for (part in parts) {
                val existingNode = currentNode.children.find { it.name == part }
                currentNode = if (existingNode == null) {
                    val newNode = Node(part, mutableListOf())
                    currentNode.children.add(newNode)
                    newNode
                } else {
                    existingNode
                }
            }
        }
        return rootNode.children
    }
}
