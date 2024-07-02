package nl.avisi.structurizr.site.generatr.site.model

import assertk.all
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.index
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import kotlin.test.Test

class SoftwareSystemContainerSectionsPageViewModelTest : ViewModelTest() {
    private val generatorContext = generatorContext()
    private val softwareSystem = generatorContext.workspace.model.addSoftwareSystem("Software system").also {
        it.addContainer("API Application")
    }
    private val container = softwareSystem.containers.first()

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
    fun `hidden view`() {
        val viewModel = SoftwareSystemContainerSectionsPageViewModel(
            generatorContext,
            softwareSystem.addContainer("Container 2")
        )

        assertThat(viewModel.visible).isFalse()
    }
}
