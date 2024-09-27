package nl.avisi.structurizr.site.generatr.site.model

import nl.avisi.structurizr.site.generatr.site.GeneratorContext
import nl.avisi.structurizr.site.generatr.site.model.indexing.*

class SearchViewModel(generatorContext: GeneratorContext) : PageViewModel(generatorContext) {
    override val pageSubTitle = "Search results"
    override val url = url()

    val language: String = generatorContext.workspace.views
        .configuration.properties.getOrDefault("generatr.search.language", "")

    val documents = buildList {
        add(home(generatorContext.workspace.documentation, this@SearchViewModel))
        addAll(workspaceDecisions(generatorContext.workspace.documentation, this@SearchViewModel))
        addAll(workspaceSections(generatorContext.workspace.documentation, this@SearchViewModel))
        addAll(
            includedSoftwareSystems
                .flatMap {
                    buildList {
                        add(softwareSystemHome(it, this@SearchViewModel))
                        add(softwareSystemContext(it, this@SearchViewModel))
                        add(softwareSystemContainers(it, this@SearchViewModel))
                        add(softwareSystemComponents(it, this@SearchViewModel))
                        add(softwareSystemRelationships(it, this@SearchViewModel))
                        addAll(softwareSystemDecisions(it, this@SearchViewModel))
                        addAll(softwareSystemSections(it, this@SearchViewModel))
                    }
                }
                .mapNotNull { it }
        )
        addAll(
            includedSoftwareSystems
                .flatMap {
                    buildList {
                        it.containers.forEach {
                            addAll(softwareSystemContainerSections(it, this@SearchViewModel))
                            addAll(softwareSystemContainerDecisions(it, this@SearchViewModel))
                        }
                    }
                }
                .mapNotNull { it }
        )
    }.mapNotNull { it }

    companion object {
        fun url() = "/search"
    }
}
