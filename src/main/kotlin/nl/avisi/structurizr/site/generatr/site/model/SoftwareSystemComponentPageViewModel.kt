package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.model.SoftwareSystem
import nl.avisi.structurizr.site.generatr.hasComponentViews
import nl.avisi.structurizr.site.generatr.hasElementImageViews
import nl.avisi.structurizr.site.generatr.site.GeneratorContext

class SoftwareSystemComponentPageViewModel(generatorContext: GeneratorContext, softwareSystem: SoftwareSystem) :
    SoftwareSystemPageViewModel(generatorContext, softwareSystem, Tab.COMPONENT) {
    val diagrams = generatorContext.workspace.views.componentViews
        .filter { it.softwareSystem == softwareSystem }
        .sortedBy { it.key }
        .map { DiagramViewModel.forView(this, it, generatorContext.svgFactory) }
    val images = generatorContext.workspace.views.imageViews
        .filter { it.elementId == softwareSystem.id }
        .sortedBy { it.key }
    val diagramsVisible = generatorContext.workspace.views.hasComponentViews(softwareSystem)
    val imagesVisible = generatorContext.workspace.views.hasElementImageViews(softwareSystem.id)
}
