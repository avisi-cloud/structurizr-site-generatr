package nl.avisi.structurizr.site.generatr.site.model

data class LegendViewModel(
    val svg: String?,
    val widthInPixels: Int?,
    val svgLocation: ImageViewModel,
    val pngLocation: ImageViewModel,
    val pumlLocation: ImageViewModel,
)