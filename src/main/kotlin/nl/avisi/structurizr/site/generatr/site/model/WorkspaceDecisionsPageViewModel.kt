package nl.avisi.structurizr.site.generatr.site.model

import nl.avisi.structurizr.site.generatr.site.context.GeneratorContext
import nl.avisi.structurizr.site.generatr.site.formatDate

class WorkspaceDecisionsPageViewModel(generatorContext: GeneratorContext) : PageViewModel(generatorContext) {
    override val url = "/${generatorContext.currentBranch}/decisions"
    override val pageSubTitle = "Decisions"

    val decisionsTable = TableViewModel.create(this) {
        headerRow(headerCell("ID"), headerCell("Date"), headerCell("Status"), headerCell("Title"))
        generatorContext.workspace.documentation.decisions
            .sortedBy { it.id }
            .forEach {
                bodyRow(
                    cell(it.id),
                    cell(formatDate(it.date)),
                    cell(it.status),
                    cellWithLink(it.title, "$url/${it.id}")
                )
            }
    }
}
