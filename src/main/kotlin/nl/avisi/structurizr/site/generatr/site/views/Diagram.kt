package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.*
import nl.avisi.structurizr.site.generatr.site.model.DiagramViewModel
import kotlin.random.Random

fun FlowContent.diagram(viewModel: DiagramViewModel, includeZoom: Boolean) {
    val charPool: List<Char> = ('a'..'z') + ('A'..'Z')
    val diagramId = (1..15)
        .map { Random.nextInt(0, charPool.size).let { charPool[it] } }
        .joinToString("")

    if (viewModel.svg != null) {
        figure {
            style = "width: min(100%, ${viewModel.diagramWidthInPixels}px);"

            rawHtml(viewModel.svg)

            figcaption {
                +viewModel.name
                +" ["
                a(href = viewModel.svgLocation.relativeHref) { +"svg" }
                +"|"
                a(href = viewModel.pngLocation.relativeHref) { +"png" }
                +"|"
                a(href = viewModel.pumlLocation.relativeHref) { +"puml" }
                +"]"
            }


        }
        if (includeZoom) {
            div(classes = "modal") {
                attributes["id"] = diagramId
                div(classes = "modal-background") {}
                div(classes = "modal-content") {
                    div(classes = "box") {
                        rawHtml(viewModel.svg, "$diagramId-svg")
                    }
                }
                button(classes = "modal-close is-large") {
                    attributes["aria-label"] = "close"
                }
            }

            button(classes = "js-modal-trigger") {
                attributes["data-target"] = diagramId
                +"Zoom SVG"
            }

        }
    }
    else
        div(classes = "notification is-danger") {
            +"No view with key"
            span(classes = "has-text-weight-bold") { +" ${viewModel.key} " }
            +"found!"
        }
}
