package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.*
import nl.avisi.structurizr.site.generatr.site.model.DiagramIndexViewModel

fun FlowContent.diagramIndex(viewModel: DiagramIndexViewModel) {
    if(viewModel.visible) {
        p(classes = "subtitle is-6") {
            +"Index:"
        }
        ul {
            viewModel.entries.forEach {
                li {
                    a(href = "#${it.key}") {
                        +it.title
                    }
                }
            }
        }
    }
}
