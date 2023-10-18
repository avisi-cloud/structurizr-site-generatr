package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.isEqualTo
import nl.avisi.structurizr.site.generatr.normalize
import kotlin.test.Test

class SoftwareSystemContainerDecisionPageViewModelTest : ViewModelTest() {
    private val generatorContext = generatorContext()
    private val softwareSystem = generatorContext.workspace.model.addSoftwareSystem("Software System").also {
        it.addContainer("API Application")
    }
    private val container = softwareSystem.containers.first()

    @Test
    fun url() {
        val decision = createDecision()
        val viewModel = SoftwareSystemContainerDecisionPageViewModel(generatorContext, container, decision)

        assertThat(SoftwareSystemContainerDecisionPageViewModel.url(container, decision))
            .isEqualTo("/${softwareSystem.name.normalize()}/decisions/${container.name.normalize()}/${decision.id}")
        assertThat(viewModel.url)
            .isEqualTo(SoftwareSystemContainerDecisionPageViewModel.url(container, decision))
    }

    @Test
    fun `active tab`() {
        val viewModel = SoftwareSystemContainerDecisionPageViewModel(generatorContext, container, createDecision())

        assertThat(viewModel.tabs.single { it.link.active }.tab)
            .isEqualTo(SoftwareSystemPageViewModel.Tab.DECISIONS)
    }

    @Test
    fun content() {
        val decision = createDecision()
        val viewModel = SoftwareSystemContainerDecisionPageViewModel(generatorContext, container, decision)

        assertThat(viewModel.content).isEqualTo(markdownToHtml(viewModel, decision.content, svgFactory))
    }
}
