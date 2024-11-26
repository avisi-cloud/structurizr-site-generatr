package nl.avisi.structurizr.site.generatr.site.model

data class DiagramIndexViewModel(
    val diagrams: List<DiagramViewModel> = emptyList(),
    val images: List<ImageViewViewModel> = emptyList()) {
    val visible: Boolean = (diagrams.count() + images.count()) > 1
}
