package nl.avisi.structurizr.site.generatr.site.model

import nl.avisi.structurizr.site.generatr.site.GeneratorContext
import nl.avisi.structurizr.site.generatr.site.model.indexing.home
import nl.avisi.structurizr.site.generatr.site.model.indexing.softwareSystemComponents
import nl.avisi.structurizr.site.generatr.site.model.indexing.softwareSystemContainers
import nl.avisi.structurizr.site.generatr.site.model.indexing.softwareSystemContext
import nl.avisi.structurizr.site.generatr.site.model.indexing.softwareSystemDecisions
import nl.avisi.structurizr.site.generatr.site.model.indexing.softwareSystemHome
import nl.avisi.structurizr.site.generatr.site.model.indexing.softwareSystemRelationships
import nl.avisi.structurizr.site.generatr.site.model.indexing.softwareSystemSections
import nl.avisi.structurizr.site.generatr.site.model.indexing.workspaceDecisions
import nl.avisi.structurizr.site.generatr.site.model.indexing.workspaceSections

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
                        addAll(softwareSystemComponentsComponent(it, this@SearchViewModel))                        
                        add(softwareSystemRelationships(it, this@SearchViewModel))
                        addAll(softwareSystemDecisions(it, this@SearchViewModel))
                        addAll(softwareSystemSections(it, this@SearchViewModel))
                    }
                }
                .mapNotNull { it },
        )
    }.mapNotNull { it }

    companion object {
        fun url() = "/search"
    }
}
