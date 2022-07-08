package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.documentation.Decision

class WorkspaceDecisionPageViewModel {
    companion object {
        fun url(decision: Decision) = "/decisions/${decision.id}"
    }
}
