package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.*
import nl.avisi.structurizr.site.generatr.site.model.ImageViewViewModel

fun FlowContent.image(viewModel: ImageViewViewModel) {
    val dialogId = "${viewModel.key}-modal"

    figure ("nhsuk-image") {
        style = "width: fit-content;"
        attributes["id"] = viewModel.key

        p(classes = "has-text-weight-bold") { +viewModel.title }
        img("nhsuk-image__img") { src = viewModel.content }
        figcaption ("nhsuk-image__caption") {
            +viewModel.title
            if (!viewModel.description.isNullOrBlank()) {
                br
                +viewModel.description
            }

        }
    }
}
