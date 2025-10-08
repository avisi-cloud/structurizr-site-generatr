package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.structurizr.documentation.Format
import kotlin.test.Test

class SoftwareSystemContainerComponentDecisionPageViewModelTest : ViewModelTest() {
    private val generatorContext = generatorContext()
    private val softwareSystem = generatorContext.workspace.model.addSoftwareSystem("Software System")
    private val container = softwareSystem.addContainer("API Application")
    private val component = container.addComponent("Email Component")

    @Test
    fun url() {
        val decision = createDecision()

        val viewModel = SoftwareSystemContainerComponentDecisionPageViewModel(generatorContext, component, decision)

        assertThat(viewModel.url)
            .isEqualTo("/software-system/decisions/api-application/email-component/1")
    }

    @Test
    fun `active tab`() {
        val viewModel = SoftwareSystemContainerComponentDecisionPageViewModel(generatorContext, component, createDecision())

        assertThat(viewModel.tabs.single { it.link.active }.tab)
            .isEqualTo(SoftwareSystemPageViewModel.Tab.DECISIONS)
    }

    @Test
    fun content() {
        val decision = createDecision()

        val viewModel = SoftwareSystemContainerComponentDecisionPageViewModel(generatorContext, component, decision)

        assertThat(viewModel.content).isEqualTo(toHtml(viewModel, decision.content, Format.Markdown, svgFactory))
    }
}
