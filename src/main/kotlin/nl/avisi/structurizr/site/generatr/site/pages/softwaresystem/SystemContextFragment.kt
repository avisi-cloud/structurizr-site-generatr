package nl.avisi.structurizr.site.generatr.site.pages.softwaresystem

import kotlinx.html.DIV
import nl.avisi.structurizr.site.generatr.site.context.AbstractSoftwareSystemPageContext

fun DIV.systemContextFragment(context: AbstractSoftwareSystemPageContext) {
    viewsFragment(context, "System context views", context.systemContextViews)
}
