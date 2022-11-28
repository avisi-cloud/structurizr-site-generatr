package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.FlowContent
import kotlinx.html.div
import kotlinx.html.unsafe

fun FlowContent.rawHtml(html: String) {
    div {
        unsafe {
            +html
        }
    }
}
