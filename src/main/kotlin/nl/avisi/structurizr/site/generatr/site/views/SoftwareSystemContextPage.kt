package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.HTML
import nl.avisi.structurizr.site.generatr.site.model.SoftwareSystemContextPageViewModel

fun HTML.softwareSystemContextPage(viewModel: SoftwareSystemContextPageViewModel) {
    if (viewModel.visible)
        softwareSystemPage(viewModel) {
            viewModel.diagrams.forEach {
                diagram(it,viewModel.includeZoom)
            }
        }
    else
        redirectUpPage()
}
