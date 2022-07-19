package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.isEqualTo
import nl.avisi.structurizr.site.generatr.normalize
import kotlin.test.Test

class SoftwareSystemDecisionsPageViewModelTest : ViewModelTest() {
    private val generatorContext = generatorContext()
    private val softwareSystem = generatorContext.workspace.model.addSoftwareSystem("Software system")

    @Test
    fun `active tab`() {
        val viewModel = SoftwareSystemDecisionsPageViewModel(generatorContext, softwareSystem)

        assertThat(viewModel.tabs.single { it.link.active }.tab)
            .isEqualTo(SoftwareSystemPageViewModel.Tab.DECISIONS)
    }

    @Test
    fun `decisions table`() {
        softwareSystem.documentation.addDecision(createDecision("1", "Accepted"))

        val viewModel = SoftwareSystemDecisionsPageViewModel(generatorContext, softwareSystem)

        assertThat(viewModel.decisionsTable)
            .isEqualTo(
                viewModel.createDecisionsTableViewModel(softwareSystem.documentation.decisions) {
                    "/${softwareSystem.name.normalize()}/decisions/1"
                }
            )
    }
}
