package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.documentation.Section
import com.structurizr.model.Component
import com.structurizr.model.Container
import nl.avisi.structurizr.site.generatr.hasSections
import nl.avisi.structurizr.site.generatr.normalize
import nl.avisi.structurizr.site.generatr.site.GeneratorContext

abstract class BaseSoftwareSystemContainerSectionsPageViewModel(generatorContext: GeneratorContext, container: Container) :
    BaseSoftwareSystemSectionsPageViewModel(generatorContext, container.softwareSystem) {

    override val visible = container.hasSections(recursive = true)

    val sectionsTable: TableViewModel = createSectionsTableViewModel(container.documentation.sections, dropFirst = false) {
        sectionTableItemUrl(container, it)
    }

    val componentSectionsTabs = container.components.filter { it.hasSections() }.map { component ->
        SectionTabViewModel(this, component.name, componentSectionItemUrl(component))
    }

    abstract fun sectionTableItemUrl(container: Container, section: Section): String
    abstract fun componentSectionItemUrl(component: Component): String
}

class SoftwareSystemContainerSectionsPageViewModel(generatorContext: GeneratorContext, container: Container) :
    BaseSoftwareSystemContainerSectionsPageViewModel(generatorContext, container) {

    override val url = url(container)

    override fun sectionTableItemUrl(container: Container, section: Section): String = url(container, section)
    override fun componentSectionItemUrl(component: Component): String = SoftwareSystemContainerComponentSectionsPageViewModel.url(component)

    companion object {
        fun url(container: Container) = "${url(container.softwareSystem, Tab.SECTIONS)}/${container.name.normalize()}"
        fun url(container: Container, section: Section) = "${url(container)}/${section.contentTitle().normalize()}"
    }
}
