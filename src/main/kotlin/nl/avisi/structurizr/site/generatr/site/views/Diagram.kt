package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.*
import nl.avisi.structurizr.site.generatr.site.model.DiagramViewModel

fun FlowContent.diagram(viewModel: DiagramViewModel) {
    val diagramWidthInPixels = "viewBox=\"\\d+ \\d+ (\\d+) \\d+\"".toRegex()
        .find(viewModel.svg)
        ?.let { it.groupValues[1].toInt() }
        ?: throw IllegalStateException("No viewBox attribute found in SVG!")

    figure {
        style = "width: min(100%, ${diagramWidthInPixels}px);"

        unsafe {
            +viewModel.svg
        }
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
