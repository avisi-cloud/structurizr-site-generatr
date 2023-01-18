package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.*
import nl.avisi.structurizr.site.generatr.site.asUrlRelativeTo
import nl.avisi.structurizr.site.generatr.site.model.PageViewModel

fun HEAD.autoReloadScript(viewModel: PageViewModel) {
    script(
        type = ScriptType.textJavaScript,
        src = "../" + "/auto-reload.js".asUrlRelativeTo(viewModel.url, appendSlash = false)
    ) { }
}

fun BODY.updatingSiteProgressBar() {
    div(classes = "is-overlay is-front-centered") {
        progress(classes = "progress is-small mt-1 is-hidden") {
            id = "updating-site"
            value = "0"
        }
    }
}

fun BODY.updateSiteErrorHero() {
    div(classes = "container is-hidden") {
        id = "update-site-error"
        div(classes = "hero is-danger mt-6") {
            div(classes = "hero-body") {
                p(classes = "title") {
                    text("Error loading workspace file")
                }
                p(classes = "subtitle") {
                    id = "update-site-error-message"
                }
            }
        }
    }
}
