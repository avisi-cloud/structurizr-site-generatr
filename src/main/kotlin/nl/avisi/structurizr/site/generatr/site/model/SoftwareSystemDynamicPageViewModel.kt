package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.model.SoftwareSystem
import nl.avisi.structurizr.site.generatr.hasComponentViews
import nl.avisi.structurizr.site.generatr.site.GeneratorContext

class SoftwareSystemDynamicPageViewModel(generatorContext: GeneratorContext, softwareSystem: SoftwareSystem) :
    SoftwareSystemPageViewModel(generatorContext, softwareSystem, Tab.COMPONENT) {

    var diagrams = List<DiagramViewModel>()

    var dia = generatorContext.workspace.model.getSoftwareSystemWithName(softwareSystem.name)?.containers?.forEach(){ container ->
        diagrams = diagrams + generatorContext.workspace.views.dynamicViews
            .filter { it.softwareSystem.name == container.name }
            .sortedBy { it.key }
            .map { DiagramViewModel.forView(this, it, generatorContext.svgFactory) }
    }

    val visible = diagrams.isNotEmpty()
}
