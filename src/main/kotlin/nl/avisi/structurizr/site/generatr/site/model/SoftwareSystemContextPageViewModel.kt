package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.model.SoftwareSystem
import nl.avisi.structurizr.site.generatr.site.context.GeneratorContext

class SoftwareSystemContextPageViewModel(generatorContext: GeneratorContext, softwareSystem: SoftwareSystem) :
    SoftwareSystemPageViewModel(generatorContext, softwareSystem, Tab.SYSTEM_CONTEXT) {
    val diagrams = generatorContext.workspace.views.systemContextViews
        .filter { it.softwareSystem == softwareSystem }
        .sortedBy { it.key }
        .map {
            DiagramViewModel(
                it.name,
                ImageViewModel(this, "/svg/${it.key}.svg"),
                ImageViewModel(this, "/png/${it.key}.png"),
                ImageViewModel(this, "/puml/${it.key}.puml")
            )
        }
}
