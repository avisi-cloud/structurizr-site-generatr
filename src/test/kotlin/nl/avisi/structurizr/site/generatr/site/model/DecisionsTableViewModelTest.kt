package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.isEqualTo
import java.time.LocalDate
import java.time.Month
import kotlin.test.Test

class DecisionsTableViewModelTest : ViewModelTest() {

    @Test
    fun `no decisions available`() {
        assertThat(pageViewModel().createDecisionsTableViewModel(emptySet()) { "href" })
            .isEqualTo(
                TableViewModel.create {
                    decisionsTableHeaderRow()
                }
            )
    }

    @Test
    fun `many decisions sorted by integer value of id`() {
        val decision1 = createDecision("10", "Accepted", LocalDate.of(2022, Month.JANUARY, 1))
        val decision2 = createDecision("2", "Proposed", LocalDate.of(2022, Month.JANUARY, 2))

        val pageViewModel = pageViewModel()
        assertThat(pageViewModel.createDecisionsTableViewModel(setOf(decision1, decision2)) { it.id }).isEqualTo(
            TableViewModel.create {
                decisionsTableHeaderRow()
                bodyRow(
                    cellWithIndex("2"),
                    cell("02-01-2022"),
                    cell("Proposed"),
                    cellWithLink(pageViewModel, "Decision 2", decision2.id)
                )
                bodyRow(
                    cellWithIndex("10"),
                    cell("01-01-2022"),
                    cell("Accepted"),
                    cellWithLink(pageViewModel, "Decision 10", decision1.id)
                )
            }
        )
    }

    private fun TableViewModel.TableViewInitializerContext.decisionsTableHeaderRow() {
        headerRow(headerCell("ID"), headerCell("Date"), headerCell("Status"), headerCell("Title"))
    }
}
