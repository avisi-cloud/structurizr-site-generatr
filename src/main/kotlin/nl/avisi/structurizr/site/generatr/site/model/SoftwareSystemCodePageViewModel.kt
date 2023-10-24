package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.model.SoftwareSystem
import nl.avisi.structurizr.site.generatr.getImagesForComponent
import nl.avisi.structurizr.site.generatr.site.GeneratorContext

class SoftwareSystemCodePageViewModel(generatorContext: GeneratorContext, softwareSystem: SoftwareSystem) :
    SoftwareSystemPageViewModel(generatorContext, softwareSystem, Tab.CODE) {
    val images = getImagesForComponent(generatorContext,softwareSystem)
    val imagesVisible = images.isNotEmpty()
}


