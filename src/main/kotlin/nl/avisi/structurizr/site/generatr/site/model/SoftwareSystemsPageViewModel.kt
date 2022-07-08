package nl.avisi.structurizr.site.generatr.site.model

import nl.avisi.structurizr.site.generatr.site.context.GeneratorContext

class SoftwareSystemsPageViewModel(generatorContext: GeneratorContext) : PageViewModel(generatorContext) {
    override val url = url()
    override val pageSubTitle = "Software Systems"

    val softwareSystemsTable: TableViewModel = TableViewModel.create {
        headerRow(headerCell("Name"), headerCell("Description"))

        generatorContext.workspace.model.softwareSystems
            .sortedBy { it.name.lowercase() }
            .forEach {
                bodyRow(
                    headerCellWithLink(
                        this@SoftwareSystemsPageViewModel, it.name, SoftwareSystemHomePageViewModel.url(it)
                    ),
                    cell(it.description)
                )
            }
    }

    companion object {
        fun url() = "/software-systems"
    }
}
