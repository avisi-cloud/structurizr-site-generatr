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
                    raw(
                        """
                    var elm = document.getElementById("${diagramId}");
                    elm.setAttribute("style", "overflow:hidden;");
                    var panZoomBox_${diagramId} = panzoom(elm, {
                        maxZoom: 5,
                        minZoom: 1,
                        transformOrigin: {x: 0.5, y: 0.5},
                        beforeWheel: function(e) {
                            var shouldIgnore = !e.ctrlKey;
                            return shouldIgnore;
                        }
                    });
            """
                    )
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
