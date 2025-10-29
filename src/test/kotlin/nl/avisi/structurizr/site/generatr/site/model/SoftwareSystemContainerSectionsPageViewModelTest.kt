package nl.avisi.structurizr.site.generatr.site.model

import assertk.all
import assertk.assertThat
import assertk.assertions.*
import kotlin.test.BeforeTest
import kotlin.test.Test

class SoftwareSystemContainerSectionsPageViewModelTest : ViewModelTest() {
    private val generatorContext = generatorContext()
    private val softwareSystem = generatorContext.workspace.model.addSoftwareSystem("Software System").apply {
        documentation.addSection(createSection())
        documentation.addSection(createSection())
    }
    private val container = softwareSystem.addContainer("API Application")

    @Test
    fun `active tab`() {
        val viewModel = SoftwareSystemContainerSectionsPageViewModel(generatorContext, container)
        assertThat(viewModel.tabs.single { it.link.active }.tab)
            .isEqualTo(SoftwareSystemPageViewModel.Tab.SECTIONS)
    }

    @Test
    fun `sections table`() {
        container.documentation.addSection(createSection())

        val viewModel = SoftwareSystemContainerSectionsPageViewModel(generatorContext, container)

        assertThat(viewModel.sectionsTable.bodyRows).all {
            hasSize(1)
            index(0).transform { (it.columns[1] as TableViewModel.LinkCellViewModel).link }
                .isEqualTo(
                    LinkViewModel(
                        viewModel,
                        "Content",
                        "/software-system/sections/api-application/content"
                    )
                )
        }
    }

    @Test
    fun `sections tabs`() {
        container.documentation.addSection(createSection())

        val viewModel = SoftwareSystemContainerSectionsPageViewModel(generatorContext, container)

        assertThat(viewModel.sectionsTabs).all {
            hasSize(2)
            index(0).all {
                transform { it.link.active }.isFalse()
                transform { it.link.title }.isEqualTo("System")
            }
            index(1).all {
                transform { it.link.active }.isTrue()
                transform { it.link }.isEqualTo(
                    LinkViewModel(
                        viewModel,
                        "API Application",
                        "/software-system/sections/api-application",
                        Match.CHILD
                    )
                )
            }
        }
    }

    @Test
    fun `component sections tabs`() {
        container.documentation.addSection(createSection())
        container.addComponent("Some Component").apply {
            documentation.addSection(createSection())
        }

        val viewModel = SoftwareSystemContainerSectionsPageViewModel(generatorContext, container)

        assertThat(viewModel.componentSectionsTabs).all {
            hasSize(2)
            index(0).all {
                transform { it.link.active }.isTrue()
                transform { it.title }.isEqualTo("Container")
            }
            index(1).all {
                transform { it.link }.isEqualTo(
                    LinkViewModel(
                        viewModel,
                        "Some Component",
                        "/software-system/sections/api-application/some-component"
                    )
                )
            }
        }
    }

    @Test
    fun `has sections`() {
        container.documentation.addSection(createSection())

        val viewModel = SoftwareSystemContainerSectionsPageViewModel(generatorContext, container)

        assertThat(viewModel.visible).isTrue()
        assertThat(viewModel.onlyComponentsDocumentationSectionsVisible).isFalse()
    }

    @Test
    fun `no sections`() {
        val viewModel = SoftwareSystemContainerSectionsPageViewModel(generatorContext, container)

        assertThat(viewModel.visible).isFalse()
        assertThat(viewModel.onlyComponentsDocumentationSectionsVisible).isFalse()
    }

    @Test
    fun `child has section`() {
        container.addComponent("Email Component").documentation.addSection(createSection())

        val viewModel = SoftwareSystemContainerSectionsPageViewModel(generatorContext, container)

        assertThat(viewModel.visible).isTrue()
        assertThat(viewModel.onlyComponentsDocumentationSectionsVisible).isTrue()
    }
}
