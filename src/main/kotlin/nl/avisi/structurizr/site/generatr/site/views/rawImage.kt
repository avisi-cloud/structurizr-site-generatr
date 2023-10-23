package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.*

fun FlowContent.rawImage(data: String) {
    img {
        src = data
    }
}
