package nl.avisi.structurizr.site.generatr.site.views

import com.structurizr.view.ImageView
import kotlinx.html.*

fun FlowContent.image(image: ImageView) {
    div(classes = "has-text-centered") {
        p(classes = "has-text-weight-bold"){+image.title}
        img {
            src = image.content
        }
        p{+image.description}
    }
}
