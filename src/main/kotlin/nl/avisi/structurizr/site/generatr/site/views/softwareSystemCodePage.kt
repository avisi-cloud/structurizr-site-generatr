package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.HTML
import nl.avisi.structurizr.site.generatr.site.model.SoftwareSystemCodePageViewModel

fun HTML.softwareSystemCodePage(viewModel: SoftwareSystemCodePageViewModel) {
    if (viewModel.visible) {
        softwareSystemPage(viewModel) {
            viewModel.images.forEach { image(it) }
        }
    } else
        redirectUpPage()
}
