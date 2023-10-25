package nl.avisi.structurizr.site.generatr.site.views

import com.structurizr.view.ImageView
import kotlinx.html.*

fun FlowContent.image(image: ImageView) {
    div {
        img {
            src = image.content
        }
        p{image.title}
        p{image.description}
    }
}
