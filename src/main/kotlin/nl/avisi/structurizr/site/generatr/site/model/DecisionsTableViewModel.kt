package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.documentation.Decision
import nl.avisi.structurizr.site.generatr.site.formatDate

fun PageViewModel.createDecisionsTableViewModel(decisions: Collection<Decision>, hrefFactory: (Decision) -> String) =
    TableViewModel.create {
        headerRow(headerCell("ID"), headerCell("Date"), headerCell("Status"), headerCell("Title"))
        decisions
            .sortedBy { it.id.toInt() }
            .forEach {
                bodyRow(
                    headerCell(it.id),
                    cell(formatDate(it.date)),
                    cell(it.status),
                    cellWithLink(this@createDecisionsTableViewModel, it.title, hrefFactory(it))
                )
            }
    }
