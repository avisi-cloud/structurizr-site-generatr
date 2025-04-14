package nl.avisi.structurizr.site.generatr.site.model

data class IndexEntry(val key: String, val title: String)

data class DiagramIndexViewModel(
    private val diagrams: List<DiagramViewModel> = emptyList(),
    private val images: List<ImageViewViewModel> = emptyList()) {

    val entries = diagrams.map { IndexEntry(it.key, it.indexTitle()) } +
            images.map { IndexEntry(it.key, it.indexTitle()) }

    val visible: Boolean = (diagrams.count() + images.count()) > 1

    private fun DiagramViewModel.indexTitle() = if (description.isNullOrBlank()) title else "$title ($description)"
    private fun ImageViewViewModel.indexTitle() = if (description.isNullOrBlank()) title else "$title ($description)"
}
