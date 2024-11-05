package nl.avisi.structurizr.site.generatr.site.model


data class DiagramIndexListViewModel(
    val diagrams: List<DiagramViewModel>?,
    val images: List<ImageViewViewModel>?,
    val showList: Boolean = ((diagrams?.count() ?: 0) + (images?.count() ?: 0)) > 1
)
