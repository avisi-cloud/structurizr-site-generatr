package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.HEAD
import kotlinx.html.link
import nl.avisi.structurizr.site.generatr.site.model.FaviconViewModel

fun HEAD.favicon(viewModel: FaviconViewModel) {
    link(
        rel = "icon",
        type = viewModel.type,
        href = viewModel.url
    )
}
