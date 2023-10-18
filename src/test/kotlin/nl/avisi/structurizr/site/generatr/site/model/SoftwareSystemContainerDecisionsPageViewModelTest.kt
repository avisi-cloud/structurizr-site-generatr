package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import nl.avisi.structurizr.site.generatr.normalize
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

        assertThat(viewModel.decisionsTable)
            .isEqualTo(
                viewModel.createDecisionsTableViewModel(container.documentation.decisions) {
                    "/${softwareSystem.name.normalize()}/decisions/${container.name.normalize()}/1"
                }
            )
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
