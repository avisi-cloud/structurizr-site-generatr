package nl.avisi.structurizr.site.generatr.site.context

import com.structurizr.documentation.Decision

class WorkspaceDecisionPageContext(
    generatorContext: GeneratorContext,
    val decision: Decision
) :
    AbstractPageContext(
        generatorContext,
        decision.title,
        "decisions/${decision.id}/index.html"
    ),
    WorkspaceDecisionsContext
