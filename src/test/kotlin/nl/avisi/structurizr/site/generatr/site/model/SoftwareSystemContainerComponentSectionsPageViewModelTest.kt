package nl.avisi.structurizr.site.generatr.site.model

import assertk.all
import assertk.assertThat
import assertk.assertions.*
import kotlin.test.BeforeTest
import kotlin.test.Test

class SoftwareSystemContainerComponentSectionsPageViewModelTest : ViewModelTest() {
    private val generatorContext = generatorContext()
    private val softwareSystem = generatorContext.workspace.model.addSoftwareSystem("Software system").also {
        it.documentation.addSection(createSection())
        it.documentation.addSection(createSection())

        it.addContainer("API Application").apply {
            documentation.addSection(createSection())

            addComponent("Some Component").apply {
                documentation.addSection(createSection())
            }
        }
    }

    private val component = softwareSystem.containers.first().components.first()

    @Test
    fun `active tab documentation`() {
        val viewModel = SoftwareSystemContainerComponentSectionsPageViewModel(generatorContext, component)
        assertThat(viewModel.tabs.single { it.link.active }.tab).isEqualTo(SoftwareSystemPageViewModel.Tab.SECTIONS)
    }

    @Test
    fun `active tab component`() {
        val viewModel = SoftwareSystemContainerComponentSectionsPageViewModel(generatorContext, component)
        assertThat(viewModel.sectionsTabs.single { it.link.active }.title).isEqualTo("API Application")
        assertThat(viewModel.sectionsTabs.single { it.title == "System" }.link.active).isFalse()
    }

    @Test
    fun `active tab component section`() {
        val viewModel = SoftwareSystemContainerComponentSectionsPageViewModel(generatorContext, component)
        assertThat(viewModel.componentSectionsTabs.single { it.link.active }.title).isEqualTo("Some Component")
    }

    @Test
    fun `component section table`() {
        val viewModel = SoftwareSystemContainerComponentSectionsPageViewModel(generatorContext, component)

        assertThat(viewModel.componentSectionTable.bodyRows).all {
            hasSize(1)
            index(0).transform { (it.columns[1] as TableViewModel.LinkCellViewModel).link }
                .isEqualTo(
                    LinkViewModel(
                        viewModel,
                        "Content",
                        "/software-system/sections/api-application/some-component/content"
                    )
                )
        }
    }
}
