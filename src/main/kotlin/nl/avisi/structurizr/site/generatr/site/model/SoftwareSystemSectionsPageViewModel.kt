package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.model.Container
import com.structurizr.model.SoftwareSystem
import nl.avisi.structurizr.site.generatr.hasContainerDocumentationSections
import nl.avisi.structurizr.site.generatr.hasDocumentationSections
import nl.avisi.structurizr.site.generatr.normalize
import nl.avisi.structurizr.site.generatr.site.GeneratorContext

abstract class BaseSoftwareSystemSectionsPageViewModel(generatorContext: GeneratorContext, softwareSystem: SoftwareSystem) :
    SoftwareSystemPageViewModel(generatorContext, softwareSystem, Tab.SECTIONS) {

    val sectionsTabs: List<SectionTabViewModel> = createSectionsTabViewModel(softwareSystem, Tab.SECTIONS) {
        if (it is Container) Match.CHILD else Match.EXACT
    }
}

class SoftwareSystemSectionsPageViewModel(
    generatorContext: GeneratorContext,
    softwareSystem: SoftwareSystem
) : BaseSoftwareSystemSectionsPageViewModel(
    generatorContext,
    softwareSystem
) {
    private val softwareSystemDocumentationSectionsVisible = softwareSystem.hasDocumentationSections()
    private val containerDocumentationSectionsVisible = softwareSystem.hasContainerDocumentationSections(recursive = true)

    val visible = softwareSystemDocumentationSectionsVisible or containerDocumentationSectionsVisible
    val onlyContainersDocumentationSectionsVisible = !softwareSystemDocumentationSectionsVisible and containerDocumentationSectionsVisible

    val sectionsTable = createSectionsTableViewModel(softwareSystem.documentation.sections) {
        "$url/${it.contentTitle().normalize()}"
    }
}
