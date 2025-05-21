package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.*
import nl.avisi.structurizr.site.generatr.site.model.SoftwareSystemPageViewModel

fun HTML.softwareSystemPage(viewModel: SoftwareSystemPageViewModel, block: FlowContent.() -> Unit) {
    page(viewModel) {
        h2(){
            span {
                role = "text"
                +viewModel.pageSubTitle
                span("nhsuk-caption-xl nhsuk-caption--bottom") {
                    span("nhsuk-u-visually-hidden") {
                        +"-"
                    }
                    +viewModel.description
                }
            }
        }

        nav(classes="") {
            role = "navigation"
            attributes["aria-label"] = "Page navigation"
            h2("nhsuk-u-visually-hidden") {
                +"Contents"
            }
            ul("app-page-navigation-list") {
                viewModel.tabs
                    .filter { it.visible }
                    .forEach {
                        li(classes=if (it.link.active) "app-page-navigation-list--current" else "app-page-navigation-list__item") {
                            link(it.link,"app-page-navigation-list-nav__link")
                        }
                    }
            }
        }
        contentDiv {
            block()
        }
    }
}
