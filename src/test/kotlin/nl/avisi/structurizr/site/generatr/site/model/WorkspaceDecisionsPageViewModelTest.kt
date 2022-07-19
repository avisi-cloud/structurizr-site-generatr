package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.isEqualTo
import java.time.LocalDate
import java.time.Month
import kotlin.test.Test

class WorkspaceDecisionsPageViewModelTest : ViewModelTest() {
    @Test
    fun url() {
        assertThat(WorkspaceDecisionsPageViewModel.url())
            .isEqualTo("/decisions")
    }

    @Test
    fun `decisions table`() {
        val generatorContext = generatorContext()
        val decision1 = createDecision("1", "Accepted", LocalDate.of(2022, Month.JANUARY, 1))
        val decision2 = createDecision("2", "Proposed", LocalDate.of(2022, Month.JANUARY, 2))
        generatorContext.workspace.documentation.addDecision(decision1)
        generatorContext.workspace.documentation.addDecision(decision2)

        val viewModel = WorkspaceDecisionsPageViewModel(generatorContext)

        assertThat(viewModel.decisionsTable).isEqualTo(
            viewModel.createDecisionsTableViewModel(generatorContext.workspace.documentation.decisions) {
                WorkspaceDecisionPageViewModel.url(it)
            }
        )
    }
}
