package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.*
import nl.avisi.structurizr.site.generatr.site.model.ImageViewViewModel

fun FlowContent.image(viewModel: ImageViewViewModel) {
    val dialogId = "${viewModel.key}-modal"

    figure {
        style = "width: fit-content;"
        attributes["id"] = viewModel.key
        
        p(classes = "has-text-weight-bold") { +viewModel.title }
        img { src = viewModel.content }
        figcaption {
            a {
                onClick = "openModal('$dialogId')"
                +viewModel.name
                if (!viewModel.description.isNullOrBlank()) {
                    br
                    +viewModel.description
                }
            }
        }
    }
    modal(dialogId) {
        img(classes = "modal-box-content modal-image") { src = viewModel.content }
        div(classes = "has-text-centered") { +viewModel.name }
    }
}
