package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.*
import nl.avisi.structurizr.site.generatr.site.model.DiagramViewModel

fun FlowContent.diagram(viewModel: DiagramViewModel) {
    if (viewModel.svg != null)
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
    else
        div(classes = "notification is-danger") {
            +"No view with key"
            span(classes = "has-text-weight-bold") { +" ${viewModel.key} " }
            +"found!"
        }
}
