package nl.avisi.structurizr.site.generatr.site.model

import nl.avisi.structurizr.site.generatr.site.asUrlRelativeTo

data class ImageViewModel(val pageViewModel: PageViewModel, val href: String) {
    val relativeHref get() = href.asUrlRelativeTo(pageViewModel.url, appendSlash = false)
}
