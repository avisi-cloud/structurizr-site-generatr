package nl.avisi.structurizr.site.generatr.site.model

import nl.avisi.structurizr.site.generatr.site.asUrlRelativeTo

data class LinkViewModel(
    private val pageViewModel: PageViewModel,
    val title: String,
    val href: String,
    val exact: Boolean = true
) {
    val relativeHref get() = href.asUrlRelativeTo(pageViewModel.url)
    val active get() = if (exact) isHrefOfContainingPage else isChildHrefOfContainingPage

    private val isHrefOfContainingPage get() = href == pageViewModel.url
    private val isChildHrefOfContainingPage get() = pageViewModel.url.startsWith(href)
}
