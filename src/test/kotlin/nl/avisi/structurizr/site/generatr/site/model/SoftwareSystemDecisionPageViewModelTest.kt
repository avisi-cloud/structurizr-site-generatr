package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.isEqualTo
import nl.avisi.structurizr.site.generatr.normalize
import kotlin.test.Test

class SoftwareSystemDecisionPageViewModelTest : ViewModelTest() {
    private val generatorContext = generatorContext()
    private val softwareSystem = generatorContext.workspace.model.addSoftwareSystem("Software System")

    @Test
    fun url() {
        val decision = createDecision()
        val viewModel = SoftwareSystemDecisionPageViewModel(generatorContext, softwareSystem, decision)

        assertThat(SoftwareSystemDecisionPageViewModel.url(softwareSystem, decision))
            .isEqualTo("/${softwareSystem.name.normalize()}/decisions/${decision.id}")
        assertThat(viewModel.url)
            .isEqualTo(SoftwareSystemDecisionPageViewModel.url(softwareSystem, decision))
    }

    @Test
    fun `active tab`() {
        val viewModel = SoftwareSystemDecisionPageViewModel(generatorContext, softwareSystem, createDecision())

        assertThat(viewModel.tabs.single { it.link.active }.tab)
            .isEqualTo(SoftwareSystemPageViewModel.Tab.DECISIONS)
    }

    @Test
    fun content() {
        val decision = createDecision()
        val viewModel = SoftwareSystemDecisionPageViewModel(generatorContext, softwareSystem, decision)

        assertThat(viewModel.content).isEqualTo(MarkdownViewModel(decision.content, svgFactory))
    }
}
