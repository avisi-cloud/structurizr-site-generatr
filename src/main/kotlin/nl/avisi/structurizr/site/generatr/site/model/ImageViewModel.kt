package nl.avisi.structurizr.site.generatr.site.model

import nl.avisi.structurizr.site.generatr.site.asUrlToFile

data class ImageViewModel(val pageViewModel: PageViewModel, val href: String) {
    val relativeHref get() = href.asUrlToFile(pageViewModel.url)
}
