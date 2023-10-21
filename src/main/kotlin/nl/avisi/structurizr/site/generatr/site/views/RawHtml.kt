package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.*

fun FlowContent.rawHtml(html: String, contentId: String? = null, contentClass: String? = null) {
    div {
        if (contentId != null) id = contentId
        if (contentClass != null) classes = setOf(contentClass)

        unsafe {
            +html
        }
    }
}
