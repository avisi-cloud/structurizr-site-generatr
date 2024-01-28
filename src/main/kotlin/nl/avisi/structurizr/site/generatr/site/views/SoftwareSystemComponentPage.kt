package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.HTML
import nl.avisi.structurizr.site.generatr.site.model.SoftwareSystemComponentPageViewModel

fun HTML.softwareSystemComponentPage(viewModel: SoftwareSystemComponentPageViewModel) {
    if (viewModel.visible)
        softwareSystemPage(viewModel) {
            // TODO: group by containers
            viewModel.diagrams.forEach { diagram(it) }
            viewModel.images.forEach { image(it) }
        }
    else
        redirectUpPage()
}
