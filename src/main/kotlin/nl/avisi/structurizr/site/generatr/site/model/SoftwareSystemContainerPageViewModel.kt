package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.model.SoftwareSystem
import nl.avisi.structurizr.site.generatr.hasContainerViews
import nl.avisi.structurizr.site.generatr.listIndexViewEnabled
import nl.avisi.structurizr.site.generatr.site.GeneratorContext

class SoftwareSystemContainerPageViewModel(generatorContext: GeneratorContext, softwareSystem: SoftwareSystem) :
    SoftwareSystemPageViewModel(generatorContext, softwareSystem, Tab.CONTAINER) {
    val diagrams = generatorContext.workspace.views.containerViews
        .filter { it.softwareSystem == softwareSystem }
        .sortedBy { it.key }
        .map { DiagramViewModel.forView(this, it, generatorContext.svgFactory) }
    val images = generatorContext.workspace.views.imageViews
        .filter { it.element == softwareSystem }
        .sortedBy { it.key }
        .map { ImageViewViewModel(it) }
    val visible = generatorContext.workspace.views.hasContainerViews(generatorContext.workspace, softwareSystem) || images.isNotEmpty()
    val diagramIndexListViewModel = DiagramIndexListViewModel(
        diagrams,
        images,
        generatorContext.workspace.listIndexViewEnabled(diagrams, images)
    )
}
