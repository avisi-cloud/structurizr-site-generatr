package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.HTML
import kotlinx.html.h3
import nl.avisi.structurizr.site.generatr.site.model.SoftwareSystemContainerDecisionPageViewModel

fun HTML.softwareSystemContainerDecisionPage(viewModel: SoftwareSystemContainerDecisionPageViewModel) {
    softwareSystemPage(viewModel) {
        rawHtml(viewModel.content)
    }
}
