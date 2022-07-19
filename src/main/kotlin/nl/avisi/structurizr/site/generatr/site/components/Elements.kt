package nl.avisi.structurizr.site.generatr.site.components

import kotlinx.html.*

fun DIV.contentDiv(block: DIV.() -> Unit) {
    div(classes = "content p-3") {
        block()
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
