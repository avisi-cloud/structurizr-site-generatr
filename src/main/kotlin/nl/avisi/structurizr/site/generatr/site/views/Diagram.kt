package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.*
import nl.avisi.structurizr.site.generatr.site.ExporterType
import nl.avisi.structurizr.site.generatr.site.model.DiagramViewModel

fun FlowContent.diagram(viewModel: DiagramViewModel) {
    when (viewModel.type) {
        ExporterType.C4, ExporterType.STRUCTURIZR -> this.addSvg(viewModel)
        ExporterType.D2 -> this.addD2(viewModel)
    }
}

private fun FlowContent.addSvg(viewModel: DiagramViewModel) {
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
        modal(dialogId) {
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
    } else {
        div(classes = "notification is-danger") {
            +"No view with key"
            span(classes = "has-text-weight-bold") { +" ${viewModel.key} " }
            +"found!"
        }
    }
}

private fun FlowContent.addD2(viewModel: DiagramViewModel) {
    val dialogId = "${viewModel.key}-modal"
    val svgId = "${viewModel.key}-svg"

    figure {
        style = "width: min(100%, ${viewModel.diagramWidthInPixels}px);"
        attributes["id"] = viewModel.key

        pre {
            classes = setOf("d2")
            if (viewModel.svg != null) attributes["page"] = viewModel.svg
            +"Generating figure..."
        }

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
}
