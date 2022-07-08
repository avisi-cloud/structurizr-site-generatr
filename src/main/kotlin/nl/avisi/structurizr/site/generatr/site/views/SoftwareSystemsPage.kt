package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.HTML
import kotlinx.html.h2
import nl.avisi.structurizr.site.generatr.site.components.contentDiv
import nl.avisi.structurizr.site.generatr.site.model.SoftwareSystemsPageViewModel

fun HTML.softwareSystemsPage(viewModel: SoftwareSystemsPageViewModel) {
    page(viewModel = viewModel) {
        contentDiv {
            h2 { +viewModel.pageSubTitle }
            table(viewModel.softwareSystemsTable)
        }
    }
}
