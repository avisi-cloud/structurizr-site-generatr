package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.model.Container
import nl.avisi.structurizr.site.generatr.site.GeneratorContext

class SoftwareSystemComponentsPageViewModel(generatorContext: GeneratorContext, container: Container)
    : SoftwareSystemPageViewModel(generatorContext, container.softwareSystem, Tab.COMPONENT) {
        val redirectLink: String? = container.name
    }
