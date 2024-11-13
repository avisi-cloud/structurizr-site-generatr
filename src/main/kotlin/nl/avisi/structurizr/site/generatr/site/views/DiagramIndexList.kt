package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.*
import nl.avisi.structurizr.site.generatr.site.model.DiagramIndexViewModel

fun FlowContent.diagramIndex(viewModel: DiagramIndexViewModel) {
    if(viewModel.visible) {
        h6 {
            +"Jump to: "
        }
        ul {
            viewModel.diagrams.forEach {
                li {
                    a(href = "#${it.key}") {
                        +(it.title ?: it.name)
                    }
                }
            }
            viewModel.images.forEach {
                li {
                    a(href = "#${it.key}") {
                        +it.title
                    }
                }
            }
        }
    }
}
