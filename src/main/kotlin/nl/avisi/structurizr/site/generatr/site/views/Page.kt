package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.*
import nl.avisi.structurizr.site.generatr.site.asUrlToFile
import nl.avisi.structurizr.site.generatr.site.model.PageViewModel
import nl.avisi.structurizr.site.generatr.site.model.Theme

fun HTML.page(viewModel: PageViewModel, block: DIV.() -> Unit) {
    attributes["lang"] = "en"
    when (viewModel.theme) {
        Theme.LIGHT -> {
            attributes["data-theme"] = "light"
        }
        Theme.DARK -> {
            attributes["data-theme"] = "dark"
        }
        Theme.AUTO -> { }
    }

    headFragment(viewModel)
    bodyFragment(viewModel, block)
}

private fun HTML.headFragment(viewModel: PageViewModel) {
    head {
        meta(charset = "utf-8")
        meta(name = "viewport", content = "width=device-width, initial-scale=1")
        title { +viewModel.pageTitle }
        link(rel = "stylesheet", href = "https://service-manual.nhs.uk/css/main.css")
        link(rel = "stylesheet", href = "../" + "/style.css".asUrlToFile(viewModel.url))
        link(rel = "stylesheet", href = "./" + "/style-branding.css".asUrlToFile(viewModel.url))
        script(type = ScriptType.textJavaScript, src = "../" + "/modal.js".asUrlToFile(viewModel.url)) { }
        script(type = ScriptType.textJavaScript, src = "../" + "/svg-modal.js".asUrlToFile(viewModel.url)) { }
        script(type = ScriptType.textJavaScript, src = viewModel.cdn.svgpanzoomJs()) { }

        if (viewModel.includeTreeview)
            link(rel = "stylesheet", href = "../" + "/treeview.css".asUrlToFile(viewModel.url))

        if (viewModel.includeAdmonition)
            markdownAdmonitionStylesheet(viewModel)

        if (viewModel.includeKatex)
            katexStylesheet(viewModel.cdn)
        if (viewModel.includeKatex)
            katexScript(viewModel.cdn)
        if (viewModel.includeKatex)
            katexFonts(viewModel.cdn)

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

        pageHeader(viewModel.headerBar)



        div(classes = "nhsuk-width-container app-width-container") {
            id = "site"
            div(classes = "nhsuk-main-wrapper"){
                id = "maintcontent"
                role = "main"
                div(classes = "nhsuk-grid-row"){
                    div(classes = "nhsuk-grid-column-one-quarter"){
                        menu(viewModel.menu, viewModel.includeTreeview)
                    }
                    div(classes = "nhsuk-grid-column-three-quarters"){
                        block()
                    }
                }
            }
        }

        footer(){
            role = "contentinfo"
            div(classes = "nhsuk-footer-container"){
                div(classes = "nhsuk-width-container"){
                    div() {
                        p(classes = "nhsuk-footer__copyright") {
                            +"NHS England"
                        }
                    }
                }
            }
        }

        if (viewModel.includeAdmonition)
            markdownAdmonitionScript(viewModel)

        mermaidScript(viewModel)

        if (viewModel.includeTreeview) {
            script(type = ScriptType.textJavaScript, src = "../" + "/treeview.js".asUrlToFile(viewModel.url)) { }
            script(type = ScriptType.textJavaScript) { unsafe { +"listree();" } }
        }

        if (viewModel.includeKatex)
            katexRenderScript(viewModel)
    }
}
