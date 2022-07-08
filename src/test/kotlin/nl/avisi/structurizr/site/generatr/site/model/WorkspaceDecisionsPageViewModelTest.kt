package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.structurizr.documentation.Decision
import com.structurizr.documentation.Format
import java.time.LocalDate
import java.time.Month
import java.time.ZoneId
import java.util.*
import kotlin.test.Test

class WorkspaceDecisionsPageViewModelTest : ViewModelTest() {
    @Test
    fun url() {
        assertThat(WorkspaceDecisionsPageViewModel.url())
            .isEqualTo("/decisions")
    }

    @Test
    fun `no workspace-level decisions available`() {
        val generatorContext = generatorContext()
        val viewModel = WorkspaceDecisionsPageViewModel(generatorContext)

        assertThat(viewModel.decisionsTable).isEqualTo(
            TableViewModel.create {
                decisionsTableHeaderRow()
            }
        )
    }

    @Test
    fun `many workspace-level decisions available`() {
        val generatorContext = generatorContext()
        val decision1 = createDecision("1", "Accepted", LocalDate.of(2022, Month.JANUARY, 1))
            .also { generatorContext.workspace.documentation.addDecision(it) }
        val decision2 = createDecision("2", "Proposed", LocalDate.of(2022, Month.JANUARY, 2))
            .also { generatorContext.workspace.documentation.addDecision(it) }
        val viewModel = WorkspaceDecisionsPageViewModel(generatorContext)

        assertThat(viewModel.decisionsTable).isEqualTo(
            TableViewModel.create {
                decisionsTableHeaderRow()
                bodyRow(
                    cell("1"),
                    cell("01-01-2022"),
                    cell("Accepted"),
                    cellWithLink(viewModel, "Decision 1", WorkspaceDecisionPageViewModel.url(decision1))
                )
                bodyRow(
                    cell("2"),
                    cell("02-01-2022"),
                    cell("Proposed"),
                    cellWithLink(viewModel, "Decision 2", WorkspaceDecisionPageViewModel.url(decision2))
                )
            }
        )
    }

    private fun createDecision(id: String, decisionStatus: String, decisionDate: LocalDate = LocalDate.now()) =
        Decision(id).apply {
            title = "Decision $id"
            status = decisionStatus
            date = Date.from(decisionDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
            format = Format.Markdown
            content = "Decision $id content"
        }

    private fun TableViewModel.TableViewInitializerContext.decisionsTableHeaderRow() {
        headerRow(headerCell("ID"), headerCell("Date"), headerCell("Status"), headerCell("Title"))
    }
}
