package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.model.SoftwareSystem
import nl.avisi.structurizr.site.generatr.hasComponentViews
import nl.avisi.structurizr.site.generatr.site.GeneratorContext

class SoftwareSystemComponentPageViewModel(generatorContext: GeneratorContext, softwareSystem: SoftwareSystem) :
    SoftwareSystemPageViewModel(generatorContext, softwareSystem, Tab.COMPONENT) {
    val diagrams = generatorContext.workspace.views.componentViews
        .filter { it.softwareSystem == softwareSystem }
        .sortedBy { it.key }
        .map { DiagramViewModel.forView(this, it, generatorContext.svgFactory) }
    val images = generatorContext.workspace.views.imageViews
        .filter { it.elementId in softwareSystem.containers.map { c -> c.id } }
        .sortedBy { it.key }
    val visible = generatorContext.workspace.views.hasComponentViews(generatorContext.workspace, softwareSystem) || images.isNotEmpty()
}
