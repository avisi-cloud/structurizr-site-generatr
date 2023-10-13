package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.*
import nl.avisi.structurizr.site.generatr.site.asUrlToFile
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
        title { +viewModel.pageTitle }
        link(rel = "stylesheet", href = "https://cdn.jsdelivr.net/npm/bulma@0.9.3/css/bulma.min.css")
        link(rel = "stylesheet", href = "../" + "/style.css".asUrlToFile(viewModel.url))
        link(rel = "stylesheet", href = "./" + "/style-branding.css".asUrlToFile(viewModel.url))

        if (viewModel.includeTreeview)
            link(rel = "stylesheet", href = "../" + "/treeview.css".asUrlToFile(viewModel.url))

        if (viewModel.includeAdmonition)
            markdownAdmonitionStylesheet(viewModel)

        if (viewModel.includeKatex)
            katexStylesheet()
        if (viewModel.includeKatex)
            katexScript()
        if (viewModel.includeKatex)
            katexFonts()

        if (viewModel.favicon.includeFavicon)
            favicon(viewModel.favicon)

        if (viewModel.includeAutoReloading)
            autoReloadScript(viewModel)

        if (viewModel.customStylesheet.includeCustomStylesheet)
            link(rel = "stylesheet", href = viewModel.customStylesheet.resourceURI)
    }
}

private fun HTML.bodyFragment(viewModel: PageViewModel, block: DIV.() -> Unit) {
    body {
        if (viewModel.includeAutoReloading)
            updatingSiteProgressBar()

        pageHeader(viewModel.headerBar)

        div(classes = "site-layout") {
            id = "site"
            menu(viewModel.menu, viewModel.includeTreeview)
            div(classes = "container is-fluid has-background-white") {
                block()
            }
        }

        if (viewModel.includeAutoReloading)
            updateSiteErrorHero()
        if (viewModel.includeAdmonition)
            markdownAdmonitionScript(viewModel)

        mermaidScript(viewModel)

        if (viewModel.includeTreeview){
            script(type = ScriptType.textJavaScript,src = "../" + "/treeview.js".asUrlToFile(viewModel.url)) { }
            script(type = ScriptType.textJavaScript) { unsafe { +"listree();" } }
        }
    }
}
