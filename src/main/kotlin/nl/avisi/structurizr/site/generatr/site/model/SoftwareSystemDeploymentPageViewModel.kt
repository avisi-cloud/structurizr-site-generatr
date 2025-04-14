package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.model.SoftwareSystem
import nl.avisi.structurizr.site.generatr.hasDeploymentViews
import nl.avisi.structurizr.site.generatr.site.GeneratorContext

class SoftwareSystemDeploymentPageViewModel(generatorContext: GeneratorContext, softwareSystem: SoftwareSystem) :
    SoftwareSystemPageViewModel(generatorContext, softwareSystem, Tab.DEPLOYMENT) {
    val diagrams = generatorContext.workspace.views.deploymentViews
        .filter {
            it.softwareSystem == softwareSystem || it.properties["generatr.view.deployment.belongsTo"] == softwareSystem.name
        }
        .map { DiagramViewModel.forView(this, it, generatorContext.svgFactory) }
    val visible = generatorContext.workspace.views.hasDeploymentViews(softwareSystem)
    val diagramIndex = DiagramIndexViewModel(diagrams)
}
