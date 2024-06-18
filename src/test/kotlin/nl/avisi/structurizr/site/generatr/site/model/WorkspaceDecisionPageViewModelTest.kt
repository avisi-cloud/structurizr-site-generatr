package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.structurizr.documentation.Format
import kotlin.test.Test

class WorkspaceDecisionPageViewModelTest : ViewModelTest() {
    @Test
    fun url() {
        val decision = createDecision()

        assertThat(WorkspaceDecisionPageViewModel.url(decision))
            .isEqualTo("/decisions/${decision.id}")
        assertThat(WorkspaceDecisionPageViewModel(generatorContext(), decision).url)
            .isEqualTo(WorkspaceDecisionPageViewModel.url(decision))
    }

    @Test
    fun subtitle() {
        val decision = createDecision()
        val viewModel = WorkspaceDecisionPageViewModel(generatorContext(), decision)

        assertThat(viewModel.pageSubTitle).isEqualTo(decision.title)
    }

    @Test
    fun content() {
        val decision = createDecision()
        val viewModel = WorkspaceDecisionPageViewModel(generatorContext(), decision)

        assertThat(viewModel.content).isEqualTo(toHtml(viewModel, decision.content, Format.Markdown, svgFactory))
    }

    @Test
    fun `absolute link`() {
        val decision = createDecision().apply {
            content = """
                [Test Link](/test/decision/2/)
            """.trimIndent()
        }
        val viewModel = WorkspaceDecisionPageViewModel(generatorContext(), decision)

        assertThat(viewModel.content).isEqualTo(
            """
                <p><a href="../../test/decision/2/">Test Link</a></p>
            """.trimIndent()
        )
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
        val viewModel = WorkspaceDecisionPageViewModel(generatorContext(), decision)

        assertThat(viewModel.content).isEqualTo(
            toHtml(
                viewModel, """
                    Decision with [link to other ADR](decisions/2).
                    [Web link](https://google.com)
                    [Internal link](#other-section)
                """.trimIndent(),
                Format.Markdown,
                svgFactory
            )
        )
    }
}
