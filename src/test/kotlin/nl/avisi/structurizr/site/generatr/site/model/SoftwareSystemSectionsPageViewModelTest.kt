package nl.avisi.structurizr.site.generatr.site.model

import assertk.all
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.index
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
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
        listOf("# Section 0000", "# Section 0001")
            .forEach { softwareSystem.documentation.addSection(createSection(it)) }

        val viewModel = SoftwareSystemSectionsPageViewModel(generatorContext, softwareSystem)

        assertThat(viewModel.sectionsTable.bodyRows).all {
            hasSize(1)
            index(0).transform { (it.columns[1] as TableViewModel.LinkCellViewModel).link }
                .isEqualTo(
                    LinkViewModel(
                        viewModel,
                        "Section 0001",
                        "/software-system/sections/section-0001"
                    )
                )
        }
    }

    @Test
    fun `has sections`() {
        listOf("# Section 0000", "# Section 0001")
            .forEach { softwareSystem.documentation.addSection(createSection(it)) }

        val viewModel = SoftwareSystemSectionsPageViewModel(generatorContext, softwareSystem)

        assertThat(viewModel.visible).isTrue()
        assertThat(viewModel.onlyContainersDocumentationSectionsVisible).isFalse()
    }

    @Test
    fun `no sections`() {
        val viewModel = SoftwareSystemSectionsPageViewModel(
            generatorContext,
            generatorContext.workspace.model.addSoftwareSystem("Software system 2")
        )

        assertThat(viewModel.visible).isFalse()
        assertThat(viewModel.onlyContainersDocumentationSectionsVisible).isFalse()
    }

    @Test
    fun `child has section`() {
        val container = softwareSystem.addContainer("API Application")
        container.documentation.addSection(createSection())

        val viewModel = SoftwareSystemSectionsPageViewModel(generatorContext, softwareSystem)

        assertThat(viewModel.visible).isTrue()
        assertThat(viewModel.onlyContainersDocumentationSectionsVisible).isTrue()
    }
}
