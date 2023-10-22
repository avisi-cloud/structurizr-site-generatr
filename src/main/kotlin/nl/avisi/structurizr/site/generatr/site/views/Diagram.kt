package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.*
import nl.avisi.structurizr.site.generatr.site.model.DiagramViewModel

fun FlowContent.diagram(viewModel: DiagramViewModel) {
    if (viewModel.svg != null) {
        val dialogId = "${viewModel.key}-modal"
        val svgId = "${viewModel.key}-svg"

        figure {
            style = "width: min(100%, ${viewModel.diagramWidthInPixels}px);"

            rawHtml(viewModel.svg)
            figcaption {
                a {
                    onClick = "openModal('$dialogId', '$svgId')"
                    +viewModel.name
                }
            }
        }
        svgModal(dialogId, svgId, viewModel)
    } else
        div(classes = "notification is-danger") {
            +"No view with key"
            span(classes = "has-text-weight-bold") { +" ${viewModel.key} " }
            +"found!"
        }
}

private fun FlowContent.svgModal(
    dialogId: String,
    svgId: String,
    viewModel: DiagramViewModel
) {
    div(classes = "modal") {
        id = dialogId

        div(classes = "modal-background") {
            onClick = "closeModal('$dialogId')"
        }
        div(classes = "modal-content") {
            div(classes = "box") {
                rawHtml(viewModel.svg!!, svgId, "modal-box-content")
                div(classes = "has-text-centered") {
                    +" ["
                    a(href = viewModel.svgLocation.relativeHref, target = "_blank") { +"svg" }
                    +"|"
                    a(href = viewModel.pngLocation.relativeHref, target = "_blank") { +"png" }
                    +"|"
                    a(href = viewModel.pumlLocation.relativeHref, target = "_blank") { +"puml" }
                    +"]"
                }
            }
        }
        button(classes = "modal-close is-large") {
            attributes["aria-label"] = "close"
            onClick = "closeModal('$dialogId')"
        }
    }
}
