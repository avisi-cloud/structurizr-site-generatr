package nl.avisi.structurizr.site.generatr.site.model

data class MarkdownViewModel(val markdown: String, val svgFactory: (key: String, url: String) -> String)
