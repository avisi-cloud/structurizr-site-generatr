package nl.avisi.structurizr.site.generatr.site.pages.softwaresystem

import com.structurizr.view.View
import kotlinx.html.DIV
import kotlinx.html.h1
import nl.avisi.structurizr.site.generatr.site.components.contentDiv
import nl.avisi.structurizr.site.generatr.site.components.diagram
import nl.avisi.structurizr.site.generatr.site.context.AbstractPageContext

fun DIV.viewsFragment(context: AbstractPageContext, title: String, views: List<View>) {
    contentDiv {
        if (views.isNotEmpty()) {
            h1 { +title }
            views.sortedBy { it.name }.forEach {
                diagram(context, it)
            }
        }
    }
}
