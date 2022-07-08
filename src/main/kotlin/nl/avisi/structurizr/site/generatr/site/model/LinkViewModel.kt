package nl.avisi.structurizr.site.generatr.site.model

import nl.avisi.structurizr.site.generatr.site.asUrlRelativeTo

data class LinkViewModel(
    private val pageViewModel: PageViewModel,
    val title: String,
    val href: String,
    val active: Boolean = false
) {
    val relativeHref get() = href.asUrlRelativeTo(pageViewModel.url)
}
