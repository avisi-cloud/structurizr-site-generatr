package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.HTML
import nl.avisi.structurizr.site.generatr.site.model.SoftwareSystemContainerSectionPageViewModel

fun HTML.softwareSystemContainerSectionPage(viewModel: SoftwareSystemContainerSectionPageViewModel) {
    softwareSystemPage(viewModel) {
        rawHtml(viewModel.content)
    }
}
