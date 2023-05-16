package nl.avisi.structurizr.site.generatr.site.model.indexing

import kotlinx.serialization.Serializable

@Serializable
data class Document(val href: String, val type: String, val title: String, val text: String)
