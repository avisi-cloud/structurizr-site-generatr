package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.FlowContent
import kotlinx.html.br
import kotlinx.html.figcaption
import kotlinx.html.figure
import kotlinx.html.img
import kotlinx.html.p
import kotlinx.html.style
import nl.avisi.structurizr.site.generatr.site.model.ImageViewViewModel

fun FlowContent.image(viewModel: ImageViewViewModel) {
    figure {
        style = "width: fit-content;"

        p(classes = "has-text-weight-bold") { +viewModel.title }
        img { src = viewModel.content }
        figcaption {
            +viewModel.name
            if (!viewModel.description.isNullOrBlank()) {
                br
                +viewModel.description
            }
        }
    }
}
