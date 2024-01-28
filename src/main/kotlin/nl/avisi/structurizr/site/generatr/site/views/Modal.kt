package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.FlowContent
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.id
import kotlinx.html.onClick

fun FlowContent.modal(dialogId: String, block: FlowContent.() -> Unit) {
    div(classes = "modal") {
        id = dialogId

        div(classes = "modal-background") {
            onClick = "closeModal('$dialogId')"
        }
        div(classes = "modal-content") {
            div(classes = "box") {
                block()
            }
        }
        button(classes = "modal-close is-large") {
            attributes["aria-label"] = "close"
            onClick = "closeModal('$dialogId')"
        }
    }
}
