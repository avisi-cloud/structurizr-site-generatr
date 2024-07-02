package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.documentation.Decision
import com.structurizr.model.SoftwareSystem
import nl.avisi.structurizr.site.generatr.hasDecisions
import nl.avisi.structurizr.site.generatr.site.formatDate

fun PageViewModel.createDecisionsTableViewModel(decisions: Collection<Decision>, hrefFactory: (Decision) -> String) =
    TableViewModel.create {
        headerRow(
            headerCellSmall("ID"),
            headerCell("Date"),
            headerCell("Status"),
            headerCellLarge("Title")
        )

        decisions
            .sortedBy { it.id.toInt() }
            .forEach {
                bodyRow(
                    cellWithIndex(it.id),
                    cell(formatDate(it.date)),
                    cell(it.status),
                    cellWithLink(this@createDecisionsTableViewModel, it.title, hrefFactory(it))
                )
            }
    }

fun SoftwareSystemPageViewModel.createDecisionsTabViewModel(
    softwareSystem: SoftwareSystem,
    tab: SoftwareSystemPageViewModel.Tab
) = buildList {
    if (softwareSystem.hasDecisions()) {
        add(
            DecisionTabViewModel(
                this@createDecisionsTabViewModel,
                "System",
                SoftwareSystemPageViewModel.url(softwareSystem, tab)
            )
        )
    }
    softwareSystem
        .containers
        .filter { it.hasDecisions() }
        .map {
            DecisionTabViewModel(
                this@createDecisionsTabViewModel,
                it.name,
                SoftwareSystemContainerDecisionsPageViewModel.url(it)
            )
        }
        .forEach { add(it) }
}
