package nl.avisi.structurizr.site.generatr.site.views

import com.structurizr.view.ImageView
import kotlinx.html.*

fun FlowContent.image(image: ImageView) {
    figure {
        p(classes = "has-text-weight-bold"){+image.title}
        img {
            src = image.content
        }
        figcaption {
            +image.description
        }
    }
}
