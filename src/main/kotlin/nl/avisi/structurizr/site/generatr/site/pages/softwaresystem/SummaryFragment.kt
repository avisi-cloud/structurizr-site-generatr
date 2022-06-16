package nl.avisi.structurizr.site.generatr.site.pages.softwaresystem

import kotlinx.html.DIV
import kotlinx.html.h1
import kotlinx.html.p
import nl.avisi.structurizr.site.generatr.site.components.contentDiv
import nl.avisi.structurizr.site.generatr.site.context.AbstractSoftwareSystemPageContext

fun DIV.summaryFragment(context: AbstractSoftwareSystemPageContext) {
    contentDiv {
        h1 { +"Description" }
        p { +context.softwareSystem.description }
    }
}
