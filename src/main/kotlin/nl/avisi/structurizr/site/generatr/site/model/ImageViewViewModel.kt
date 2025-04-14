package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.model.Component
import com.structurizr.model.Container
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.ImageView

data class ImageViewViewModel(val imageView: ImageView) {
    val key: String = imageView.key
    val title: String = imageView.title ?: generateName()
    val description: String? = imageView.description
    val content: String = imageView.content

    private fun generateName(): String {
        val elementName = generateSequence(imageView.element) {
            it.parent
        }.map { it.name }.toList().reversed().joinToString(" - ")

        val elementType = when (imageView.element) {
            is SoftwareSystem -> "Containers"
            is Container -> "Components"
            is Component -> "Code"
            else -> throw IllegalStateException("Not supported element")
        }

        return "$elementName - $elementType"
    }
}
