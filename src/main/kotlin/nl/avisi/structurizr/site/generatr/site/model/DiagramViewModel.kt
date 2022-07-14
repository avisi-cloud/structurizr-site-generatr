package nl.avisi.structurizr.site.generatr.site.model

data class DiagramViewModel(
    val name: String,
    val svgLocation: ImageViewModel,
    val pngLocation: ImageViewModel,
    val pumlLocation: ImageViewModel
)
