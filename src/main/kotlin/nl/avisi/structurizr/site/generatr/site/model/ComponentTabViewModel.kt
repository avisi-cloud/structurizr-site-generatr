package nl.avisi.structurizr.site.generatr.site.model

data class ComponentTabViewModel(val pageViewModel: PageViewModel, val title: String, val url: String) {
    val link = LinkViewModel(pageViewModel, title, url)
}
