package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.HTML
import kotlinx.html.body
import kotlinx.html.head
import kotlinx.html.meta
import kotlinx.html.title

fun HTML.redirectUpPage() {
    attributes["lang"] = "en"
    head {
        meta {
            httpEquiv = "refresh"
            content = "0; url=../"
        }
        title { +"Structurizr site generatr" }
    }
    body()
}
