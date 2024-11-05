package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.model.SoftwareSystem
import nl.avisi.structurizr.site.generatr.hasDynamicViews
import nl.avisi.structurizr.site.generatr.site.GeneratorContext

class SoftwareSystemDynamicPageViewModel(generatorContext: GeneratorContext, softwareSystem: SoftwareSystem) :
    SoftwareSystemPageViewModel(generatorContext, softwareSystem, Tab.DYNAMIC) {
    val diagrams = generatorContext.workspace.views.dynamicViews
        .filter { it.softwareSystem == softwareSystem }
        .sortedBy { it.key }
        .map { DiagramViewModel.forView(this, it, generatorContext.svgFactory) }
    val visible = generatorContext.workspace.views.hasDynamicViews(softwareSystem)
    val diagramIndexListViewModel = DiagramIndexListViewModel(
        diagrams,
        emptyList()
    )
}
