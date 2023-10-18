package nl.avisi.structurizr.site.generatr.site.model

import assertk.all
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.index
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import kotlin.test.Test

class SoftwareSystemContainerDecisionsPageViewModelTest : ViewModelTest() {
    private val generatorContext = generatorContext()
    private val softwareSystem = generatorContext.workspace.model.addSoftwareSystem("Software system").also {
        it.addContainer("API Application")
    }
    private val container = softwareSystem.containers.first()

    @Test
    fun `active tab`() {
        val viewModel = SoftwareSystemContainerDecisionsPageViewModel(generatorContext, container)
        assertThat(viewModel.tabs.single { it.link.active }.tab)
            .isEqualTo(SoftwareSystemPageViewModel.Tab.DECISIONS)
    }

    @Test
    fun `decisions table`() {
        container.documentation.addDecision(createDecision("1", "Accepted"))

        val viewModel = SoftwareSystemContainerDecisionsPageViewModel(generatorContext, container)

        assertThat(viewModel.decisionsTable.bodyRows).all {
            hasSize(1)
            index(0).transform { (it.columns[3] as TableViewModel.LinkCellViewModel).link }
                .isEqualTo(
                    LinkViewModel(
                        viewModel,
                        "Decision 1",
                        "/software-system/decisions/api-application/1",
                        true
                    )
                )
        }
    }

    @Test
    fun `hidden view`() {
        val viewModel = SoftwareSystemContainerDecisionsPageViewModel(
            generatorContext,
            softwareSystem.addContainer("Container 2")
        )

        assertThat(viewModel.visible).isFalse()
    }
}
