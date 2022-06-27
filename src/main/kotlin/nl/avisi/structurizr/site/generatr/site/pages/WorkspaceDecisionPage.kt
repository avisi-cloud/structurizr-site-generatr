package nl.avisi.structurizr.site.generatr.site.pages

import kotlinx.html.HTML
import nl.avisi.structurizr.site.generatr.site.components.contentDiv
import nl.avisi.structurizr.site.generatr.site.components.page
import nl.avisi.structurizr.site.generatr.site.components.renderedMarkdown
import nl.avisi.structurizr.site.generatr.site.context.WorkspaceDecisionPageContext

fun HTML.workspaceDecisionPage(context: WorkspaceDecisionPageContext) {
    page(context) {
        contentDiv {
            renderedMarkdown(context, context.decision.content)
        }
    }
}
