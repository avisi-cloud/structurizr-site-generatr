package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.model.SoftwareSystem
import nl.avisi.structurizr.site.generatr.getImagesForSystem
import nl.avisi.structurizr.site.generatr.hasContainerViews
import nl.avisi.structurizr.site.generatr.site.GeneratorContext

class SoftwareSystemContainerPageViewModel(generatorContext: GeneratorContext, softwareSystem: SoftwareSystem) :
    SoftwareSystemPageViewModel(generatorContext, softwareSystem, Tab.CONTAINER) {
    val diagrams = generatorContext.workspace.views.containerViews
        .filter { it.softwareSystem == softwareSystem }
        .sortedBy { it.key }
        .map { DiagramViewModel.forView(this, it, generatorContext.svgFactory) }
    val images = getImagesForSystem(generatorContext,softwareSystem)
    val diagramsVisible = generatorContext.workspace.views.hasContainerViews(softwareSystem)
    val imagesVisible = images.isNotEmpty()
}
