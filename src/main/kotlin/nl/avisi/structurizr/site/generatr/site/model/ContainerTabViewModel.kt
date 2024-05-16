package nl.avisi.structurizr.site.generatr.site.model

data class ContainerTabViewModel(val pageViewModel: SoftwareSystemPageViewModel, val title: String, val url: String, val match: Match = Match.EXACT) {
    val link = LinkViewModel(pageViewModel, title, url, match)
}
