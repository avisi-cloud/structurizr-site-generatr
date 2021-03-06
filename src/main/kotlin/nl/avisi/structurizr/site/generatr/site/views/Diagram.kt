package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.*
import nl.avisi.structurizr.site.generatr.site.model.DiagramViewModel

fun FlowContent.diagram(viewModel: DiagramViewModel) {
    figure {
        img(src = viewModel.svgLocation.relativeHref, alt = viewModel.name)
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
}
