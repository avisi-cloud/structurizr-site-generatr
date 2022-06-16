package nl.avisi.structurizr.site.generatr.site.components

import kotlinx.html.*
import nl.avisi.structurizr.site.generatr.homeSection
import nl.avisi.structurizr.site.generatr.site.context.AbstractPageContext
import nl.avisi.structurizr.site.generatr.site.context.HomePageContext

fun BODY.pageHeader(context: AbstractPageContext) {
    nav(classes = "navbar is-dark") {
        role = "navigation"
        attributes["aria-label"] = "main navigation"

        div(classes = "navbar-brand") {
            a(
                classes = "navbar-item",
                href = HomePageContext(context.generatorContext, context.workspace.documentation.homeSection).url
            ) {
                span(classes = "has-text-weight-semibold") { +context.workspace.name }
            }
        }
        div(classes = "navbar-menu") {
            div(classes = "navbar-end") {
                div(classes = "navbar-item has-dropdown is-hoverable") {
                    a(classes = "navbar-link has-text-grey-light") {
                        +context.currentBranch
                    }
                    div(classes = "navbar-dropdown is-right") {
                        context.branches.forEach { branchName ->
                            a(classes = "navbar-item", href = "/$branchName") { +branchName }
                        }
                        hr(classes = "navbar-divider")
                        div(classes = "navbar-item has-text-grey-light") {
                            span { +"v" }
                            span { +context.generatorContext.version }
                        }
                    }
                }
            }
        }
    }
}
