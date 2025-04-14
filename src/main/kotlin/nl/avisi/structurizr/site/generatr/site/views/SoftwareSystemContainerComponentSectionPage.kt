package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.HTML
import nl.avisi.structurizr.site.generatr.site.model.SoftwareSystemContainerComponentSectionPageViewModel
import nl.avisi.structurizr.site.generatr.site.model.SoftwareSystemContainerSectionPageViewModel

fun HTML.softwareSystemContainerComponentSectionPage(viewModel: SoftwareSystemContainerComponentSectionPageViewModel) {
    softwareSystemPage(viewModel) {
        rawHtml(viewModel.content)
    }
}
