package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.*

fun DIV.contentDiv(block: DIV.() -> Unit) {
    div() {
        block()
    }
}
