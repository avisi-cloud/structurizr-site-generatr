package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.*
import nl.avisi.structurizr.site.generatr.site.model.DiagramIndexListViewModel
import nl.avisi.structurizr.site.generatr.site.model.DiagramViewModel

fun FlowContent.diagramIndexList(viewModel: DiagramIndexListViewModel) {
    if(viewModel.showList)
        ol {
            viewModel.diagrams.forEach {
                li {
                    a(href="#${it.key}") {
                        +(it.title ?: it.name)
                    }
                }
            }
        }
}
