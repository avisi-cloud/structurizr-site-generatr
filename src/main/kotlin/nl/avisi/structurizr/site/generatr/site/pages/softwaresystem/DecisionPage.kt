package nl.avisi.structurizr.site.generatr.site.pages.softwaresystem

import kotlinx.html.HTML
import nl.avisi.structurizr.site.generatr.site.components.contentDiv
import nl.avisi.structurizr.site.generatr.site.components.renderedMarkdown
import nl.avisi.structurizr.site.generatr.site.context.SoftwareSystemDecisionPageContext

fun HTML.softwareSystemDecisionPage(context: SoftwareSystemDecisionPageContext) {
    softwareSystemPage(context) {
        contentDiv {
            renderedMarkdown(context, context.decision.content)
        }
    }
}
