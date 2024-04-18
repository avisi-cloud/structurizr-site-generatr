package nl.avisi.structurizr.site.generatr.site.model

import nl.avisi.structurizr.site.generatr.site.asUrlToDirectory
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

data class BranchHomeLinkViewModel(
    private val pageViewModel: PageViewModel,
    private val branchName: String
) {
    val title get() = branchName
    val relativeHref get() = HomePageViewModel.url().asUrlToDirectory(pageViewModel.url) + "../${URLEncoder.encode(branchName, StandardCharsets.UTF_8)}/"
}
