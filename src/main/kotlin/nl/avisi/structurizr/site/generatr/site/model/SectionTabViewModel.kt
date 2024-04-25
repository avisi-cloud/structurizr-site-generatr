package nl.avisi.structurizr.site.generatr.site.model

data class SectionTabViewModel(val pageViewModel: SoftwareSystemPageViewModel, val title: String, val url: String) {
    val link = LinkViewModel(pageViewModel, title, url)
}
