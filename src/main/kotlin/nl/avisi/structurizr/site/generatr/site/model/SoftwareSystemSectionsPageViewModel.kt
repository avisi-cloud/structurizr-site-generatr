package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.model.SoftwareSystem
import nl.avisi.structurizr.site.generatr.hasContainerDocumentationSections
import nl.avisi.structurizr.site.generatr.hasDocumentationSections
import nl.avisi.structurizr.site.generatr.site.GeneratorContext

class SoftwareSystemSectionsPageViewModel(generatorContext: GeneratorContext, softwareSystem: SoftwareSystem) :
    SoftwareSystemPageViewModel(generatorContext, softwareSystem, Tab.SECTIONS) {
    val sectionsTable = createSectionsTableViewModel(softwareSystem.documentation.sections) {
        "$url/${it.order}"
    }

    private val containerSectionsVisible = softwareSystem.hasContainerDocumentationSections()
    val softwareSystemSectionsVisible = softwareSystem.hasDocumentationSections()

    val visible = softwareSystemSectionsVisible or containerSectionsVisible
    val onlyContainerSectionsVisible = !softwareSystemSectionsVisible and containerSectionsVisible

    val sectionTabs = createSectionsTabViewModel(softwareSystem, Tab.SECTIONS)
}
