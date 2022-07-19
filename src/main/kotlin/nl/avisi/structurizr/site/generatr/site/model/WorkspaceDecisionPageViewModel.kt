package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.documentation.Decision
import nl.avisi.structurizr.site.generatr.site.GeneratorContext

class WorkspaceDecisionPageViewModel(generatorContext: GeneratorContext, decision: Decision) :
    PageViewModel(generatorContext) {
    override val url = url(decision)
    override val pageSubTitle: String = decision.title

    val markdown = MarkdownViewModel(decision.content)

    companion object {
        fun url(decision: Decision) = "/decisions/${decision.id}"
    }
}
