package nl.avisi.structurizr.site.generatr.site.pages.softwaresystem

import kotlinx.html.DIV
import nl.avisi.structurizr.site.generatr.site.context.AbstractSoftwareSystemPageContext

fun DIV.deploymentFragment(context: AbstractSoftwareSystemPageContext) {
    viewsFragment(context, "Deployment views", context.deploymentViews)
}
