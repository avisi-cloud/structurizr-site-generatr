package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.model.SoftwareSystem
import nl.avisi.structurizr.site.generatr.hasSystemContextViews
import nl.avisi.structurizr.site.generatr.site.GeneratorContext

class SoftwareSystemContextPageViewModel(generatorContext: GeneratorContext, softwareSystem: SoftwareSystem) :
    SoftwareSystemPageViewModel(generatorContext, softwareSystem, Tab.SYSTEM_CONTEXT) {
    val diagrams = generatorContext.workspace.views.systemContextViews
        .filter { it.softwareSystem == softwareSystem }
        .sortedBy { it.key }
        .map { DiagramViewModel.forView(this, it, generatorContext.svgFactory) }
    val visible = generatorContext.workspace.views.hasSystemContextViews(softwareSystem)
    val diagramIndex = DiagramIndexViewModel(diagrams, emptyList())
}
