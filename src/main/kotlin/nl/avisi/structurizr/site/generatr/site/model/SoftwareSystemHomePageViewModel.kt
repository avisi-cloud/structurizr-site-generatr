package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.model.SoftwareSystem
import nl.avisi.structurizr.site.generatr.normalize
import nl.avisi.structurizr.site.generatr.site.context.GeneratorContext

class SoftwareSystemHomePageViewModel(generatorContext: GeneratorContext, softwareSystem: SoftwareSystem) :
    PageViewModel(generatorContext) {
    override val url: String = url(softwareSystem)
    override val pageSubTitle: String = softwareSystem.name

    val tabBar = SoftwareSystemTabBarViewModel.create(this, softwareSystem)
    val description: String = softwareSystem.description

    companion object {
        fun url(softwareSystem: SoftwareSystem) = "/${softwareSystem.name.normalize()}"
    }
}
