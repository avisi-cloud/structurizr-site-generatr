package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.*
import nl.avisi.structurizr.site.generatr.site.model.DiagramViewModel
import kotlin.random.Random

fun FlowContent.diagram(viewModel: DiagramViewModel) {
    val charPool: List<Char> = ('a'..'z') + ('A'..'Z')
    val diagramId = (1..15)
        .map { Random.nextInt(0, charPool.size).let { charPool[it] } }
        .joinToString("")

    if (viewModel.svg != null)
        figure {
            style = "width: min(100%, ${viewModel.diagramWidthInPixels}px);"

            rawHtml(viewModel.svg, diagramId)

            script(type = ScriptType.textJavaScript) {
                unsafe {
                    raw("""
                    var elm = document.getElementById("${diagramId}");
                    elm.setAttribute("style","width: min(100%, ${viewModel.diagramWidthInPixels}px); height: ${viewModel.diagramHeightInPixels}px;");
                    var svgElement = elm.firstElementChild;
                    svgElement.setAttribute("style","display: inline; width: inherit; min-width: inherit; max-width: inherit; height: inherit; min-height: inherit; max-height: inherit; ");
                    var panZoomBox_${diagramId} = svgPanZoom(svgElement, {
                        zoomEnabled: true,
                        controlIconsEnabled: true,
                        fit: true,
                        center: true,
                        minZoom: 1,
                        maxZoom: 5
                    });
                    """)
                }
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
    else
        div(classes = "notification is-danger") {
            +"No view with key"
            span(classes = "has-text-weight-bold") { +" ${viewModel.key} " }
            +"found!"
        }
}
