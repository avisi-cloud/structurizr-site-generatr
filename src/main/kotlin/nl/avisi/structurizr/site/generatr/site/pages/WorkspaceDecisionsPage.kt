package nl.avisi.structurizr.site.generatr.site.pages

import kotlinx.html.HTML
import kotlinx.html.h2
import nl.avisi.structurizr.site.generatr.site.components.contentDiv
import nl.avisi.structurizr.site.generatr.site.components.decisionsTable
import nl.avisi.structurizr.site.generatr.site.components.page
import nl.avisi.structurizr.site.generatr.site.context.WorkspaceDecisionPageContext
import nl.avisi.structurizr.site.generatr.site.context.WorkspaceDecisionsPageContext

fun HTML.workspaceDecisionsPage(context: WorkspaceDecisionsPageContext) {
    page(context) {
        contentDiv {
            h2 { +"Architecture decision records" }
            decisionsTable(context, context.workspace.documentation.decisions) { decision ->
                WorkspaceDecisionPageContext(context.generatorContext, decision)
            }
        }
    }
}
