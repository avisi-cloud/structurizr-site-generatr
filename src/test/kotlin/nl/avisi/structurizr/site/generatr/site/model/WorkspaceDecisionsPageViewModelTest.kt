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
        val generatorContext = generatorContext().apply {
            workspace.documentation.addDecision(createDecision("1", "Accepted", LocalDate.of(2022, Month.JANUARY, 1)))
            workspace.documentation.addDecision(createDecision("2", "Proposed", LocalDate.of(2022, Month.JANUARY, 2)))
        }
        val viewModel = WorkspaceDecisionsPageViewModel(generatorContext)

        assertThat(viewModel.decisionsTable).isEqualTo(
            TableViewModel.create {
                decisionsTableHeaderRow()
                bodyRow(
                    cell("1"),
                    cell("01-01-2022"),
                    cell("Accepted"),
                    cellWithLink(viewModel, "Decision 1", "/${generatorContext.currentBranch}/decisions/1")
                )
                bodyRow(
                    cell("2"),
                    cell("02-01-2022"),
                    cell("Proposed"),
                    cellWithLink(viewModel, "Decision 2", "/${generatorContext.currentBranch}/decisions/2")
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
