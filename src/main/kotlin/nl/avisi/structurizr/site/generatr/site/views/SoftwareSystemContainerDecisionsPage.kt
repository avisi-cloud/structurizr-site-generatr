package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.HTML
import kotlinx.html.h3
import nl.avisi.structurizr.site.generatr.site.model.SoftwareSystemContainerDecisionsPageViewModel

fun HTML.softwareSystemContainerDecisionsPage(viewModel: SoftwareSystemContainerDecisionsPageViewModel) {
    if (viewModel.visible)
        softwareSystemPage(viewModel) {
            h3 { +(viewModel.containerName + " Decisions") }
            table(viewModel.decisionsTable)
        }
    else
        redirectUpPage()
}
