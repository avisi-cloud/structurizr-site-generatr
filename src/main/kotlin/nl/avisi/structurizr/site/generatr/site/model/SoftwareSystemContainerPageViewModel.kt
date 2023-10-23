package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.model.SoftwareSystem
import nl.avisi.structurizr.site.generatr.hasContainerViews
import nl.avisi.structurizr.site.generatr.hasElementImageViews
import nl.avisi.structurizr.site.generatr.site.GeneratorContext

class SoftwareSystemContainerPageViewModel(generatorContext: GeneratorContext, softwareSystem: SoftwareSystem) :
    SoftwareSystemPageViewModel(generatorContext, softwareSystem, Tab.CONTAINER) {
    val diagrams = generatorContext.workspace.views.containerViews
        .filter { it.softwareSystem == softwareSystem }
        .sortedBy { it.key }
        .map { DiagramViewModel.forView(this, it, generatorContext.svgFactory) }
    val images = generatorContext.workspace.views.imageViews
        .filter { it.elementId == softwareSystem.id }
        .sortedBy { it.key }
    val diagramsVisible = generatorContext.workspace.views.hasContainerViews(softwareSystem)
    val imagesVisible = generatorContext.workspace.views.hasElementImageViews(softwareSystem.id)
}
