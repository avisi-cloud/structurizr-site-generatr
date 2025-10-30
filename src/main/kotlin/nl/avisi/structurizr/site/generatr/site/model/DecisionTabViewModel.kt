package nl.avisi.structurizr.site.generatr.site.model

data class DecisionTabViewModel(val pageViewModel: SoftwareSystemPageViewModel, val title: String, val url: String, private val match: Match = Match.EXACT) {
    val link = LinkViewModel(pageViewModel, title, url, match)
}
