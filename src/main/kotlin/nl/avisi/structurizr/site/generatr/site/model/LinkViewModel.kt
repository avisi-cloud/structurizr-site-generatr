package nl.avisi.structurizr.site.generatr.site.model

import nl.avisi.structurizr.site.generatr.site.asUrlToDirectory

data class LinkViewModel(
    private val pageViewModel: PageViewModel,
    val title: String,
    val href: String,
    val match: Match = Match.EXACT
) {
    val relativeHref get() = href.asUrlToDirectory(pageViewModel.url)
    val active get() =
        when (match) {
            Match.EXACT -> isHrefOfContainingPage
            Match.CHILD -> isChildHrefOfContainingPage
            Match.SIBLING -> isSiblingHrefOfContainingPage
        }

    private val isHrefOfContainingPage get() = href == pageViewModel.url
    private val isChildHrefOfContainingPage get() = pageViewModel.url == href || pageViewModel.url.startsWith("$href/")
    private val isSiblingHrefOfContainingPage get() = pageViewModel.url.trimEnd('/').dropLastWhile { it != '/' } == href.trimEnd('/').dropLastWhile { it != '/' }
}
enum class Match {
    EXACT,
    CHILD,
    SIBLING
}
