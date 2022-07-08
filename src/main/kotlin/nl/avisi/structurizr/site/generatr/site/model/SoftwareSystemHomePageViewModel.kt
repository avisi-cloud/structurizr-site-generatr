package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.model.SoftwareSystem
import nl.avisi.structurizr.site.generatr.normalize

class SoftwareSystemHomePageViewModel {
    companion object {
        fun url(softwareSystem: SoftwareSystem) = "/${softwareSystem.name.normalize()}"
    }
}
