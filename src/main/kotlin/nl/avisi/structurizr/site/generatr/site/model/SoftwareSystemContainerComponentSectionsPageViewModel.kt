package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.documentation.Section
import com.structurizr.model.Component
import com.structurizr.model.Container
import nl.avisi.structurizr.site.generatr.hasComponentsSections
import nl.avisi.structurizr.site.generatr.hasSections
import nl.avisi.structurizr.site.generatr.normalize
import nl.avisi.structurizr.site.generatr.site.GeneratorContext

class SoftwareSystemContainerComponentSectionsPageViewModel(generatorContext: GeneratorContext, component: Component) :
    BaseSoftwareSystemContainerSectionsPageViewModel(generatorContext, component.container) {

    override val visible = component.hasSections()
    override val url = url(component)

    override fun sectionTableItemUrl(container: Container, section: Section): String =
        SoftwareSystemContainerSectionsPageViewModel.url(container, section)

    override fun componentSectionItemUrl(component: Component): String = url(component)

    val componentSectionTable = createSectionsTableViewModel(component.documentation.sections, dropFirst = false) {
        SoftwareSystemContainerComponentSectionPageViewModel.url(component, it)
    }

    companion object {
        fun url(component: Component) =
            "${SoftwareSystemContainerSectionsPageViewModel.url(component.container)}/${component.name.normalize()}"
    }
}
