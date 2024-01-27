package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.model.SoftwareSystem
import nl.avisi.structurizr.site.generatr.site.GeneratorContext

class SoftwareSystemCodePageViewModel(generatorContext: GeneratorContext, softwareSystem: SoftwareSystem) :
    SoftwareSystemPageViewModel(generatorContext, softwareSystem, Tab.CODE) {
    val images = generatorContext.workspace.views.imageViews
        .filter { it.elementId in softwareSystem.containers.flatMap { c -> c.components.map { com -> com.id } } }
        .sortedBy { it.key }
        .map { ImageViewViewModel(it) }
    val visible = images.isNotEmpty()
}
