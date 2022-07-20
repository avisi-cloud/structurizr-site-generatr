package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.HTML
import kotlinx.html.h1
import kotlinx.html.p
import nl.avisi.structurizr.site.generatr.site.model.SoftwareSystemHomePageViewModel

fun HTML.softwareSystemHomePage(viewModel: SoftwareSystemHomePageViewModel) {
    softwareSystemPage(viewModel) {
        h1 { +"Description" }
        p { +viewModel.description }
    }
}
