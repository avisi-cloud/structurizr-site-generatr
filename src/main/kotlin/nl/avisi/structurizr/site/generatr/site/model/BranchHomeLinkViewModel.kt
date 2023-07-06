package nl.avisi.structurizr.site.generatr.site.model

import nl.avisi.structurizr.site.generatr.site.asUrlToDirectory

data class BranchHomeLinkViewModel(
    private val pageViewModel: PageViewModel,
    private val branchName: String
) {
    val title get() = branchName
    val relativeHref get() = HomePageViewModel.url().asUrlToDirectory(pageViewModel.url) + "../$branchName/"
}
