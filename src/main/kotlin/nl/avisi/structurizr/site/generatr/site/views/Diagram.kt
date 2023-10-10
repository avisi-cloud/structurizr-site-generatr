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
                br {  }
                a(classes="button is-white is-small", href = viewModel.svgLocation.relativeHref) { +"svg" }
                +" - "
                a(classes="button is-white is-small", href = viewModel.pngLocation.relativeHref) { +"png" }
                +" - "
                a(classes="button is-white is-small", href = viewModel.pumlLocation.relativeHref) { +"puml" }

                if (includeZoom) {
                    +" - "
                    button(classes = "js-modal-trigger button is-white is-small") {
                        attributes["data-target"] = diagramId
                        +"Pan / Zoom"
                    }
                }
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
        }
    }
    else
        div(classes = "notification is-danger") {
            +"No view with key"
            span(classes = "has-text-weight-bold") { +" ${viewModel.key} " }
            +"found!"
        }
}
