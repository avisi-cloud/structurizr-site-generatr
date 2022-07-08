package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.*
import nl.avisi.structurizr.site.generatr.site.components.contentDiv
import nl.avisi.structurizr.site.generatr.site.model.SoftwareSystemHomePageViewModel

fun HTML.softwareSystemHomePage(viewModel: SoftwareSystemHomePageViewModel) {
    page(viewModel) {
        h1(classes = "title mt-3") { +viewModel.pageSubTitle }
        h2(classes = "subtitle") { +viewModel.description }

        div(classes = "tabs mt-3") {
            ul {
                viewModel.tabBar.tabs.forEach {
                    li(classes = if (it.active) "is-active" else null) {
                        link(it.link)
                    }
                }
            }
        }
        contentDiv {
            h1 { +"Description" }
            p { +viewModel.description }
        }
    }
}
