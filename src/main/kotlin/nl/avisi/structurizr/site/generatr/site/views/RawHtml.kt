package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.FlowContent
import kotlinx.html.div
import kotlinx.html.unsafe
import kotlin.random.Random

fun FlowContent.rawHtml(html: String, id: String = randomId()) {
    div {
        attributes["id"] = id
        unsafe {
            +html
        }
    }
}

fun randomId(): String {
    val charPool : List<Char> = ('a'..'z') + ('A'..'Z')
    val id = (1..15)
        .map { Random.nextInt(0, charPool.size).let { charPool[it] } }
        .joinToString("")
    return id
}
