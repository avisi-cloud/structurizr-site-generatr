package nl.avisi.structurizr.site.generatr.site.components

import kotlinx.html.*
import nl.avisi.structurizr.site.generatr.site.context.AbstractPageContext

fun HTML.page(context: AbstractPageContext, contents: DIV.() -> Unit) {
    attributes["lang"] = "en"

    headFragment(context)
    bodyFragment(context, contents)
}

private fun HTML.headFragment(context: AbstractPageContext) {
    head {
        meta(charset = "utf-8")
        meta(name = "viewport", content = "width=device-width, initial-scale=1")
        link(rel = "stylesheet", href = "https://cdn.jsdelivr.net/npm/bulma@0.9.3/css/bulma.min.css")
        link(rel = "stylesheet", href = "/css/style.css")
        title { +context.workspace.name }
    }
}

private fun HTML.bodyFragment(context: AbstractPageContext, contents: DIV.() -> Unit) {
    body {
        pageHeader(context)
        div(classes = "site-layout") {
            menu(context)
            pageContents(contents)
        }
    }
}

private fun DIV.pageContents(contents: DIV.() -> Unit) {
    div(classes = "container is-fluid has-background-white") {
        contents()
    }
}
