package nl.avisi.structurizr.site.generatr.site.model

import nl.avisi.structurizr.site.generatr.site.asUrlRelativeTo

data class BranchHomeLinkViewModel(
    private val pageViewModel: PageViewModel,
    private val branchName: String
) {
    val title get() = branchName
    val relativeHref get() = "/".asUrlRelativeTo(pageViewModel.url) + "/../$branchName"
}
