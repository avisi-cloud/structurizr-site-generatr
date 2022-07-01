package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.*
import nl.avisi.structurizr.site.generatr.site.asUrlRelativeTo
import nl.avisi.structurizr.site.generatr.site.model.PageViewModel

fun HTML.page(viewModel: PageViewModel, block: DIV.() -> Unit) {
    attributes["lang"] = "en"
    classes = setOf("has-background-light")

    headFragment(viewModel)
    bodyFragment(viewModel, block)
}

private fun HTML.headFragment(viewModel: PageViewModel) {
    head {
        meta(charset = "utf-8")
        meta(name = "viewport", content = "width=device-width, initial-scale=1")
        link(rel = "stylesheet", href = "https://cdn.jsdelivr.net/npm/bulma@0.9.3/css/bulma.min.css")
        link(
            rel = "stylesheet",
            href = "/css/style.css".asUrlRelativeTo(viewModel.url)
        )
        title { +viewModel.pageTitle }
    }
}

private fun HTML.bodyFragment(viewModel: PageViewModel, block: DIV.() -> Unit) {
    body {
        pageHeader(viewModel.headerBar)

        div(classes = "site-layout") {
            menu(viewModel.menu)
            div(classes = "container is-fluid has-background-white") {
                block()
            }
        }
    }
}
