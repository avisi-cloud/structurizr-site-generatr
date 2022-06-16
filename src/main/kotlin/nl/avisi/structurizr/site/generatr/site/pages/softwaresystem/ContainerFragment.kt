package nl.avisi.structurizr.site.generatr.site.pages.softwaresystem

import kotlinx.html.DIV
import nl.avisi.structurizr.site.generatr.site.context.AbstractSoftwareSystemPageContext

fun DIV.containerFragment(context: AbstractSoftwareSystemPageContext) {
    viewsFragment(context, "Container views", context.containerViews)
}
