package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.HTML
import kotlinx.html.h2
import nl.avisi.structurizr.site.generatr.site.model.SoftwareSystemHomePageViewModel

fun HTML.softwareSystemHomePage(viewModel: SoftwareSystemHomePageViewModel) {
    softwareSystemPage(viewModel) {
        markdown(viewModel, viewModel.content)
        if (viewModel.hasProperties) {
            h2 { +"Properties" }
            table(viewModel.propertiesTable)
        }
    }
}
