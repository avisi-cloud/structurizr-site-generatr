package nl.avisi.structurizr.site.generatr.site.components

import com.structurizr.view.View
import kotlinx.html.*
import nl.avisi.structurizr.site.generatr.site.asUrlRelativeTo
import nl.avisi.structurizr.site.generatr.site.context.AbstractPageContext

fun DIV.contentDiv(block: DIV.() -> Unit) {
    div(classes = "content p-3") {
        block()
    }
}

fun DIV.diagram(context: AbstractPageContext, view: View) {
    figure {
        img(src = "${context.urlPrefix}/svg/${view.key}.svg".asUrlRelativeTo(context.url), alt = view.name)
        figcaption {
            +view.name
            +" ["
            a(href = "${context.urlPrefix}/svg/${view.key}.svg".asUrlRelativeTo(context.url)) { +"svg" }
            +"|"
            a(href = "${context.urlPrefix}/png/${view.key}.png".asUrlRelativeTo(context.url)) { +"png" }
            +"|"
            a(href = "${context.urlPrefix}/puml/${view.key}.puml".asUrlRelativeTo(context.url)) { +"puml" }
            +"]"
        }
    }
}

fun DIV.tabs(block: UL.() -> Unit) {
    div(classes = "tabs mt-3") {
        ul {
            block()
        }
    }
}

fun UL.tab(href: String, active: Boolean, block: A.() -> Unit) {
    li(classes = if (active) "is-active" else null) {
        a(href = href) {
            block()
        }
    }
}
