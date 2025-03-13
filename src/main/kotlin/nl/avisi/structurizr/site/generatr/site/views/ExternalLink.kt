package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.FlowOrPhrasingContent
import kotlinx.html.a
import nl.avisi.structurizr.site.generatr.site.model.ExternalLinkViewModel

fun FlowOrPhrasingContent.externalLink(viewModel: ExternalLinkViewModel) {
    a(
        href = viewModel.href,
        target = "_blank",
    ) {
        +viewModel.title
    }
}
