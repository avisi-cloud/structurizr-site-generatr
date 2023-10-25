package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.model.SoftwareSystem
import nl.avisi.structurizr.site.generatr.getImagesForContainer
import nl.avisi.structurizr.site.generatr.hasComponentViews
import nl.avisi.structurizr.site.generatr.site.GeneratorContext

class SoftwareSystemComponentPageViewModel(generatorContext: GeneratorContext, softwareSystem: SoftwareSystem) :
    SoftwareSystemPageViewModel(generatorContext, softwareSystem, Tab.COMPONENT) {
    val diagrams = generatorContext.workspace.views.componentViews
        .filter { it.softwareSystem == softwareSystem }
        .sortedBy { it.key }
        .map { DiagramViewModel.forView(this, it, generatorContext.svgFactory) }
    val images = getImagesForContainer(generatorContext.workspace,softwareSystem)
    val diagramsVisible = generatorContext.workspace.views.hasComponentViews(generatorContext, softwareSystem)
    val imagesVisible = images.isNotEmpty()
}


