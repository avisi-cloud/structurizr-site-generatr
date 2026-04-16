package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.*
import nl.avisi.structurizr.site.generatr.site.model.DiagramViewModel

fun FlowContent.diagram(viewModel: DiagramViewModel) {
    if (viewModel.svg != null) {
        val dialogId = "${viewModel.key}-modal"
        val svgId = "${viewModel.key}-svg"

        figure {
            style = "width: min(100%, ${viewModel.diagramWidthInPixels}px);"
            attributes["id"] = viewModel.key
            rawHtml(viewModel.svg)
            figcaption {
                a {
                    onClick = "openSvgModal('$dialogId', '$svgId')"
                    +viewModel.title
                    if (!viewModel.description.isNullOrBlank()) {
                        br
                        +viewModel.description
                    }
                }
            }
        }
        if (viewModel.hasLegend) {
            details(classes = "mt-2 mb-4") {
                style = "width: min(100%, ${viewModel.diagramWidthInPixels}px);"
                summary(classes = "is-clickable") {
                    +"Show legend"
                }
                div(classes = "mt-2") {
                    style = viewModel.legendWidthInPixels?.let {
                        "width: min(100%, ${it}px);"
                    } ?: "width: 100%;"
                    rawHtml(viewModel.legendSvg!!)
                }
            }
        }
        modal(dialogId) {
            // TODO: no links in this SVG
            rawHtml(viewModel.svg, svgId, "modal-box-content")
            if (viewModel.hasLegend) {
                div(classes = "mt-4") {
                    rawHtml(viewModel.legendSvg!!)
                }
            }
            div(classes = "has-text-centered") {
                +viewModel.title
                +" ["
                a(href = viewModel.svgLocation.relativeHref, target = "_blank") { +"svg" }
                +"|"
                a(href = viewModel.pngLocation.relativeHref, target = "_blank") { +"png" }
                +"|"
                a(href = viewModel.pumlLocation.relativeHref, target = "_blank") { +"puml" }
                +"]"
                if (viewModel.hasLegend) {
                    +" [legend: "
                    a(href = viewModel.legendSvgLocation!!.relativeHref, target = "_blank") { +"svg" }
                    +"|"
                    a(href = viewModel.legendPngLocation!!.relativeHref, target = "_blank") { +"png" }
                    +"|"
                    a(href = viewModel.legendPumlLocation!!.relativeHref, target = "_blank") { +"puml" }
                    +"]"
                }
            }
        }
    } else
        div(classes = "notification is-danger") {
            +"No view with key"
            span(classes = "has-text-weight-bold") { +" ${viewModel.key} " }
            +"found!"
        }
}
