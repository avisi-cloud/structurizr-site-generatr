package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.FlowContent
import kotlinx.html.div
import kotlinx.html.unsafe
import nl.avisi.structurizr.site.generatr.randomId

fun FlowContent.rawHtml(html: String, id: String = randomId()) {
    div {
        attributes["id"] = id
        unsafe {
            +html
        }
    }
}
