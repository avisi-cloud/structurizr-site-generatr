package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.model.Container
import nl.avisi.structurizr.site.generatr.hasSections
import nl.avisi.structurizr.site.generatr.normalize
import nl.avisi.structurizr.site.generatr.site.GeneratorContext

class SoftwareSystemContainerSectionsPageViewModel(generatorContext: GeneratorContext, container: Container) :
    SoftwareSystemPageViewModel(generatorContext, container.softwareSystem, Tab.SECTIONS) {
    override val url = url(container)
    val sectionsTable = createSectionsTableViewModel(container.documentation.sections, dropFirst = false) {
        "$url/${it.contentTitle().normalize()}"
    }

    val visible = container.hasSections()
    val sectionsTabs = createSectionsTabViewModel(container.softwareSystem, Tab.SECTIONS)

    companion object {
        fun url(container: Container) = "${url(container.softwareSystem, Tab.SECTIONS)}/${container.name.normalize()}"
    }
}
