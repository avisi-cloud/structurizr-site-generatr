package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.isEqualTo
import nl.avisi.structurizr.site.generatr.normalize
import nl.avisi.structurizr.site.generatr.site.model.SoftwareSystemPageViewModel.Companion.url
import nl.avisi.structurizr.site.generatr.site.model.SoftwareSystemPageViewModel.Tab
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
            .isEqualTo(Tab.DECISIONS)
    }

    @Test
    fun content() {
        val decision = createDecision()
        val viewModel = SoftwareSystemDecisionPageViewModel(generatorContext, softwareSystem, decision)

        assertThat(viewModel.content).isEqualTo(markdownToHtml(viewModel, decision.content, svgFactory))
    }


    @Test
    fun `link to other ADR`() {
        val decision = createDecision().apply {
            content = """
                Decision with [link to other ADR](#2).
                [Web link](https://google.com)
                [Internal link](#other-section)
            """.trimIndent()
        }
        val viewModel = SoftwareSystemDecisionPageViewModel(generatorContext, softwareSystem, decision)

        assertThat(viewModel.content).isEqualTo(
            markdownToHtml(
                viewModel, """
                    Decision with [link to other ADR](${url(softwareSystem, Tab.DECISIONS)}/2).
                    [Web link](https://google.com)
                    [Internal link](#other-section)
                """.trimIndent(),
                svgFactory
            )
        )
    }
}
