package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.HTML
import nl.avisi.structurizr.site.generatr.site.model.SoftwareSystemSectionPageViewModel

fun HTML.softwareSystemSectionPage(viewModel: SoftwareSystemSectionPageViewModel) {
    softwareSystemPage(viewModel) {
        markdown(viewModel, viewModel.content)
    }
}
