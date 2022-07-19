package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.*
import nl.avisi.structurizr.site.generatr.site.model.SoftwareSystemPageViewModel

fun HTML.softwareSystemPage(viewModel: SoftwareSystemPageViewModel, block: FlowContent.() -> Unit) {
    page(viewModel) {
        h1(classes = "title mt-3") { +viewModel.pageSubTitle }
        h2(classes = "subtitle") { +viewModel.description }

        div(classes = "tabs mt-3") {
            ul {
                viewModel.tabs
                    .filter { it.visible }
                    .forEach {
                        li(classes = if (it.link.active) "is-active" else null) {
                            link(it.link)
                        }
                    }
            }
        }
        contentDiv {
            block()
        }
    }
}
