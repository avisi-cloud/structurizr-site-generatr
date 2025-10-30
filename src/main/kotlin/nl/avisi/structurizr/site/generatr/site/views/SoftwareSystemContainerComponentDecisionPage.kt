package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.HTML
import nl.avisi.structurizr.site.generatr.site.model.SoftwareSystemContainerComponentDecisionPageViewModel

fun HTML.softwareSystemContainerComponentDecisionPage(viewModel: SoftwareSystemContainerComponentDecisionPageViewModel) {
    softwareSystemPage(viewModel) {
        rawHtml(viewModel.content)
    }
}
