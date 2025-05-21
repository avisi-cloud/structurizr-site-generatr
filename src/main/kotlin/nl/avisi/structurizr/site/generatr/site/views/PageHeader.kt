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
    header(classes = "nhsuk-header app-header__container") {
        role = "banner"
        attributes["aria-label"] = "main header"

        div(classes = "nhsuk-header__container"){
            div(classes = "nhsuk-header__logo"){
                a(classes = "nhsuk-header__link nhsuk-header__link--service", href = "/") {
                    attributes["aria-label"] = "NSP Solution Architecture Home Page"
                    unsafe {
                        +"""
                            <svg class="nhsuk-logo" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 40 16" height="40" width="100">
                              <path class="nhsuk-logo__background" fill="#005eb8" d="M0 0h40v16H0z"></path>
                              <path class="nhsuk-logo__text" fill="#fff" d="M3.9 1.5h4.4l2.6 9h.1l1.8-9h3.3l-2.8 13H9l-2.7-9h-.1l-1.8 9H1.1M17.3 1.5h3.6l-1 4.9h4L25 1.5h3.5l-2.7 13h-3.5l1.1-5.6h-4.1l-1.2 5.6h-3.4M37.7 4.4c-.7-.3-1.6-.6-2.9-.6-1.4 0-2.5.2-2.5 1.3 0 1.8 5.1 1.2 5.1 5.1 0 3.6-3.3 4.5-6.4 4.5-1.3 0-2.9-.3-4-.7l.8-2.7c.7.4 2.1.7 3.2.7s2.8-.2 2.8-1.5c0-2.1-5.1-1.3-5.1-5 0-3.4 2.9-4.4 5.8-4.4 1.6 0 3.1.2 4 .6"></path>
                            </svg>
                        """
                    }
                    span (classes = "nhsuk-header__service-name"){
                        +"National Screening Platform - Solution Architecture"
                    }
                }
            }
            div(classes = "nhsuk-header__content"){
                div(classes = "nhsuk-header__search"){
                    div(classes = "nhsuk-header__search-wrap"){
                        form(
                                classes = "nhsuk-header__search-form",
                                action = "/search/",
                                method = FormMethod.get
                        ) {
                            id = "search"
                            attributes["role"] = "search"

                            label(classes = "nhsuk-u-visually-hidden") {
                                htmlFor = "search-field"
                                +"Search the NHS website"
                            }

                            input(classes = "nhsuk-search__input") {
                                id = "search-field"
                                name = "search-field"
                                type = InputType.search
                                placeholder = "Search"
                                autoComplete = "off"
                            }

                            button(classes = "nhsuk-search__submit") {
                                type = ButtonType.submit

                                unsafe {
                                    +"""
                <svg class="nhsuk-icon nhsuk-icon__search" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" aria-hidden="true" focusable="false">
                  <path d="M19.71 18.29l-4.11-4.1a7 7 0 1 0-1.41 1.41l4.1 4.11a1 1 0 0 0 1.42 0 1 1 0 0 0 0-1.42zM5 10a5 5 0 1 1 5 5 5 5 0 0 1-5-5z"></path>
                </svg>
            """
                                }

                                span(classes = "nhsuk-u-visually-hidden") {
                                    +"Search"
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

