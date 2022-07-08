package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.structurizr.documentation.Decision
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

        assertThat(viewModel.markdown).isEqualTo(MarkdownViewModel(decision.content))
    }

    private fun createDecision(): Decision {
        val decision = Decision("1").apply {
            title = "Some decision"
            content = "Some markdown content"
        }
        return decision
    }
}
