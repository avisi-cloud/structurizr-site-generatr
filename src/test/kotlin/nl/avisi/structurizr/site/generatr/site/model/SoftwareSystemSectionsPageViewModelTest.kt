package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import nl.avisi.structurizr.site.generatr.normalize
import kotlin.test.Test

class SoftwareSystemSectionsPageViewModelTest : ViewModelTest() {
    private val generatorContext = generatorContext()
    private val softwareSystem = generatorContext.workspace.model.addSoftwareSystem("Software system")

    @Test
    fun `active tab`() {
        val viewModel = SoftwareSystemSectionsPageViewModel(generatorContext, softwareSystem)

        assertThat(viewModel.tabs.single { it.link.active }.tab)
            .isEqualTo(SoftwareSystemPageViewModel.Tab.SECTIONS)
    }

    @Test
    fun `sections table`() {
        listOf("Section 0000", "Section 0001")
            .forEach { softwareSystem.documentation.addSection(createSection(it)) }

        val viewModel = SoftwareSystemSectionsPageViewModel(generatorContext, softwareSystem)

        assertThat(viewModel.sectionsTable)
            .isEqualTo(
                viewModel.createSectionsTableViewModel(softwareSystem.documentation.sections) {
                    "/${softwareSystem.name.normalize()}/sections/2"
                }
            )
    }

    @Test
    fun `hidden view`() {
        val viewModel = SoftwareSystemSectionsPageViewModel(
            generatorContext,
            generatorContext.workspace.model.addSoftwareSystem("Software system 2")
        )

        assertThat(viewModel.visible).isFalse()
    }
}
