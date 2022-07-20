package nl.avisi.structurizr.site.generatr.site.model

import nl.avisi.structurizr.site.generatr.site.GeneratorContext

class WorkspaceDecisionsPageViewModel(generatorContext: GeneratorContext) : PageViewModel(generatorContext) {
    override val url = url()
    override val pageSubTitle = "Decisions"

    val decisionsTable = createDecisionsTableViewModel(generatorContext.workspace.documentation.decisions) {
        WorkspaceDecisionPageViewModel.url(it)
    }

    companion object {
        fun url() = "/decisions"
    }
}
