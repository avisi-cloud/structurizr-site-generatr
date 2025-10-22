package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.documentation.Section
import com.structurizr.model.Component
import com.structurizr.model.Container
import nl.avisi.structurizr.site.generatr.hasComponentsSections
import nl.avisi.structurizr.site.generatr.hasSections
import nl.avisi.structurizr.site.generatr.normalize
import nl.avisi.structurizr.site.generatr.site.GeneratorContext

abstract class BaseSoftwareSystemContainerSectionsPageViewModel(generatorContext: GeneratorContext, container: Container) :
    BaseSoftwareSystemSectionsPageViewModel(generatorContext, container.softwareSystem) {
    val sectionsTable: TableViewModel = createSectionsTableViewModel(container.documentation.sections, dropFirst = false) {
        sectionTableItemUrl(container, it)
    }

    val componentSectionsTabs by lazy {
        val components = container.components.filter { it.hasSections() }
        buildList(components.size) {
            if (container.hasSections()) {
                add(
                    SectionTabViewModel(
                        this@BaseSoftwareSystemContainerSectionsPageViewModel,
                        "Container",
                        sectionTableItemUrl(container),
                        Match.EXACT
                    )
                )
            }

            addAll(
                components.map { component ->
                    SectionTabViewModel(this@BaseSoftwareSystemContainerSectionsPageViewModel, component.name, componentSectionItemUrl(component))
                }
            )
        }
    }

    abstract fun sectionTableItemUrl(container: Container, section: Section? = null): String
    abstract fun componentSectionItemUrl(component: Component): String
}

class SoftwareSystemContainerSectionsPageViewModel(generatorContext: GeneratorContext, container: Container) :
    BaseSoftwareSystemContainerSectionsPageViewModel(generatorContext, container) {
    private val containerDocumentationSectionsVisible = container.hasSections()
    private val componentsDocumentationSectionsVisible = container.hasComponentsSections()

    val visible = containerDocumentationSectionsVisible or componentsDocumentationSectionsVisible
    val onlyComponentsDocumentationSectionsVisible = !containerDocumentationSectionsVisible and componentsDocumentationSectionsVisible

    override val url = url(container)

    override fun sectionTableItemUrl(container: Container, section: Section?): String = url(container, section)
    override fun componentSectionItemUrl(component: Component): String = SoftwareSystemContainerComponentSectionsPageViewModel.url(component)

    companion object {
        fun url(container: Container) = "${url(container.softwareSystem, Tab.SECTIONS)}/${container.name.normalize()}"
        fun url(container: Container, section: Section?) = section?.let { "${url(container)}/${it.contentTitle().normalize()}" } ?: url(container)
    }
}
