package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.documentation.Decision
import com.structurizr.model.SoftwareSystem
import nl.avisi.structurizr.site.generatr.site.GeneratorContext

class SoftwareSystemDecisionPageViewModel(
    generatorContext: GeneratorContext, softwareSystem: SoftwareSystem, decision: Decision
) : SoftwareSystemPageViewModel(generatorContext, softwareSystem, Tab.DECISIONS) {
    override val url = url(softwareSystem, decision)

    val content = markdownToHtml(this, decision.content, generatorContext.svgFactory)

    companion object {
        fun url(softwareSystem: SoftwareSystem, decision: Decision) =
            "${url(softwareSystem, Tab.DECISIONS)}/${decision.id}"
    }
}
