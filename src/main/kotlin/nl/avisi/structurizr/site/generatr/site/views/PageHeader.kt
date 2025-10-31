package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.*
import nl.avisi.structurizr.site.generatr.site.asUrlToFile
import nl.avisi.structurizr.site.generatr.site.model.HeaderBarViewModel
import nl.avisi.structurizr.site.generatr.site.model.ImageViewModel
import nl.avisi.structurizr.site.generatr.site.model.LinkViewModel

fun BODY.pageHeader(viewModel: HeaderBarViewModel) {
    script(
        type = ScriptType.textJavaScript,
        src = "../" + "/header.js".asUrlToFile(viewModel.url)
    ) { }
    nav(classes = "navbar") {
        role = "navigation"
        attributes["aria-label"] = "main navigation"

        div(classes = "navbar-brand has-site-branding") {
            // Mobile hamburger menu button (positioned first for left alignment)
            a(classes = "navbar-burger") {
                role = "button"
                attributes["aria-label"] = "menu"
                attributes["aria-expanded"] = "false"
                attributes["data-target"] = "site-menu"

                span { attributes["aria-hidden"] = "true" }
                span { attributes["aria-hidden"] = "true" }
                span { attributes["aria-hidden"] = "true" }
                span { attributes["aria-hidden"] = "true" }
            }

            if (viewModel.hasLogo)
                logo(viewModel.titleLink, viewModel.logo!!)

            navbarLink(viewModel.titleLink)
        }
        div(classes = "navbar-menu has-site-branding") {
            div(classes = "navbar-end") {
                div(classes = "navbar-item") {
                    input(classes = "input is-small is-rounded") {
                        id = "search"
                        type = InputType.search
                        size = "30"
                        maxLength = "50"
                        placeholder = "Search..."
                        onKeyUp = "redirect(event, value, '${viewModel.searchLink.relativeHref}')"
                    }
                }
                div(classes = "navbar-item has-dropdown is-hoverable") {
                    a(classes = "navbar-link has-site-branding") {
                        +viewModel.currentBranch
                    }
                    div(classes = "navbar-dropdown is-right") {
                        viewModel.branches.forEach { branchLink ->
                            a(
                                classes = "navbar-item",
                                href = branchLink.relativeHref
                            ) {
                                +branchLink.title
                            }
                        }
                        if (viewModel.allowToggleTheme) {
                            hr(classes = "navbar-divider")
                            a(
                                classes = "navbar-item",
                            ) {
                                onClick = "toggleTheme()"
                                +"Toggle theme"
                            }
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

private fun DIV.logo(linkViewModel: LinkViewModel, imageViewModel: ImageViewModel) {
    a(
        classes = "navbar-item",
        href = linkViewModel.relativeHref
    ) {
        img {
            alt = linkViewModel.title
            src = imageViewModel.relativeHref
        }
    }
}

private fun DIV.navbarLink(viewModel: LinkViewModel) {
    a(
        classes = "navbar-item",
        href = viewModel.relativeHref
    ) {
        span(classes = "has-text-weight-semibold has-site-branding") { +viewModel.title }
    }
}
