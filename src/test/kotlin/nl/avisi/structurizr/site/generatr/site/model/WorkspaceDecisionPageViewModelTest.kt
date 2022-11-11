package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.isEqualTo
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

        assertThat(viewModel.content).isEqualTo(MarkdownViewModel(decision.content, svgFactory))
    }
}
