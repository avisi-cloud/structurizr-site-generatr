package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.documentation.Decision
import com.structurizr.model.SoftwareSystem
import nl.avisi.structurizr.site.generatr.site.GeneratorContext

class SoftwareSystemDecisionPageViewModel(
    generatorContext: GeneratorContext, softwareSystem: SoftwareSystem, decision: Decision
) : SoftwareSystemPageViewModel(generatorContext, softwareSystem, Tab.DECISIONS) {
    override val url = url(softwareSystem, decision)

    val content = toHtml(
        this,
        transformADRLinks(decision.content, softwareSystem),
        decision.format,
        generatorContext.svgFactory
    )

    private fun transformADRLinks(content: String, softwareSystem: SoftwareSystem) =
        content.replace("\\[(.*)]\\(#(\\d+)\\)".toRegex()) {
            "[${it.groupValues[1]}](${url(softwareSystem, Tab.DECISIONS)}/${it.groupValues[2]})"
        }

    companion object {
        fun url(softwareSystem: SoftwareSystem, decision: Decision) =
            "${url(softwareSystem, Tab.DECISIONS)}/${decision.id}"
    }
}
