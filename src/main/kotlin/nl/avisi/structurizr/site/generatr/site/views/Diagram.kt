package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.*
import nl.avisi.structurizr.site.generatr.site.model.DiagramViewModel

fun FlowContent.diagram(viewModel: DiagramViewModel) {
    if (viewModel.svg != null) {
        val dialogId = "${viewModel.key}-modal"
        val svgId = "${viewModel.key}-svg"

        figure ("nhsuk-image") {
            style = "width: min(100%, ${viewModel.diagramWidthInPixels}px);"
            attributes["id"] = viewModel.key
            rawHtml(viewModel.svg)
            figcaption ("nhsuk-image__caption") {
                a{
                    onClick = "openSvgModal('$dialogId', '$svgId')"
                    +viewModel.title
                    if (!viewModel.description.isNullOrBlank()) {
                        br
                        +viewModel.description
                    }
                }
            }
        }
        modal(dialogId) {
            // TODO: no links in this SVG
            rawHtml(viewModel.svg, svgId, "modal-box-content")
            div(classes = "has-text-centered") {
                +viewModel.title
                +" ["
                a(href = viewModel.svgLocation.relativeHref, target = "_blank") { +"svg" }
                +"|"
                a(href = viewModel.pngLocation.relativeHref, target = "_blank") { +"png" }
                +"|"
                a(href = viewModel.pumlLocation.relativeHref, target = "_blank") { +"puml" }
                +"]"
            }
        }

    } else
        div(classes = "notification is-danger") {
            +"No view with key"
            span(classes = "has-text-weight-bold") { +" ${viewModel.key} " }
            +"found!"
        }
}
