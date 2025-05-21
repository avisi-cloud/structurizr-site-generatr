package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.FlowOrPhrasingContent
import kotlinx.html.a
import nl.avisi.structurizr.site.generatr.site.model.LinkViewModel

fun FlowOrPhrasingContent.link(viewModel: LinkViewModel, classes: String = "") {
    a(
            href = viewModel.relativeHref,
            classes = "$classes ${if (viewModel.active) "is-active" else ""}".trim()
    ) {
        +viewModel.title
    }
}
