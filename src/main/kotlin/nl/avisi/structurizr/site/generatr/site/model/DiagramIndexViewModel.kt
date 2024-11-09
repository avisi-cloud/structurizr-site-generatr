package nl.avisi.structurizr.site.generatr.site.model


data class DiagramIndexViewModel(
    val diagrams: List<DiagramViewModel>,
    val images: List<ImageViewViewModel>,
    val showList: Boolean = (diagrams.count() + images.count()) > 1
)
