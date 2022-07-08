package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.*
import nl.avisi.structurizr.site.generatr.site.model.HeaderBarViewModel
import nl.avisi.structurizr.site.generatr.site.model.LinkViewModel

fun BODY.pageHeader(viewModel: HeaderBarViewModel) {
    nav(classes = "navbar is-dark") {
        role = "navigation"
        attributes["aria-label"] = "main navigation"

        div(classes = "navbar-brand") {
            navbarLink(viewModel.titleLink)
        }
        div(classes = "navbar-menu") {
            div(classes = "navbar-end") {
                div(classes = "navbar-item has-dropdown is-hoverable") {
                    a(classes = "navbar-link has-text-grey-light") {
                        +viewModel.currentBranch
                    }
                    div(classes = "navbar-dropdown is-right") {
                        viewModel.branches.forEach { branchLink ->
                            link(viewModel = branchLink, classes = "navbar-item")
                        }
                        hr(classes = "navbar-divider")
                        div(classes = "navbar-item has-text-grey-light") {
                            span { +"v" }
                            span { +viewModel.version }
                        }
                    }
                }
            }
        }
    }
}

private fun DIV.navbarLink(viewModel: LinkViewModel) {
    a(
        classes = "navbar-item",
        href = viewModel.relativeHref
    ) {
        span(classes = "has-text-weight-semibold") { +viewModel.title }
    }
}
