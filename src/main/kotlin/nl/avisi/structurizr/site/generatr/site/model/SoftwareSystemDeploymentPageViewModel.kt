package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.model.SoftwareSystem
import nl.avisi.structurizr.site.generatr.hasDeploymentViews
import nl.avisi.structurizr.site.generatr.listIndexViewEnabled
import nl.avisi.structurizr.site.generatr.site.GeneratorContext

class SoftwareSystemDeploymentPageViewModel(generatorContext: GeneratorContext, softwareSystem: SoftwareSystem) :
    SoftwareSystemPageViewModel(generatorContext, softwareSystem, Tab.DEPLOYMENT) {
    val diagrams = generatorContext.workspace.views.deploymentViews
        .filter { it.softwareSystem == softwareSystem }
        .sortedBy { it.key }
        .map { DiagramViewModel.forView(this, it, generatorContext.svgFactory) }
    val visible = generatorContext.workspace.views.hasDeploymentViews(softwareSystem)
    val diagramIndexListViewModel = DiagramIndexListViewModel(
        diagrams,
        generatorContext.workspace.listIndexViewEnabled
    )
}
