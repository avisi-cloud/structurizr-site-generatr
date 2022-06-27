package nl.avisi.structurizr.site.generatr.site.pages.softwaresystem

import com.structurizr.documentation.Decision
import kotlinx.html.DIV
import kotlinx.html.h1
import nl.avisi.structurizr.site.generatr.site.components.contentDiv
import nl.avisi.structurizr.site.generatr.site.components.decisionsTable
import nl.avisi.structurizr.site.generatr.site.context.AbstractSoftwareSystemPageContext
import nl.avisi.structurizr.site.generatr.site.context.SoftwareSystemDecisionPageContext

fun DIV.decisionListFragment(context: AbstractSoftwareSystemPageContext) {
    contentDiv {
        h1 { +"Architecture decision records" }
        decisionsTable(context, context.adrs) { decision: Decision ->
            SoftwareSystemDecisionPageContext(
                context.generatorContext,
                context.softwareSystem,
                decision
            )
        }
    }
}
