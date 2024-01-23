package nl.avisi.structurizr.site.generatr.site.views

import com.structurizr.view.ImageView
import kotlinx.html.FlowContent
import kotlinx.html.figcaption
import kotlinx.html.figure
import kotlinx.html.img
import kotlinx.html.p

fun FlowContent.image(image: ImageView) {
    figure {
        p(classes = "has-text-weight-bold") { +image.title }
        img {
            src = image.content
        }
        figcaption {
            +image.description
        }
    }
}
