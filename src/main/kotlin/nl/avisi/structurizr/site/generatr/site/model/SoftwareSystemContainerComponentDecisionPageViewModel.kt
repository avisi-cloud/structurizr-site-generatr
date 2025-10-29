package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.documentation.Decision
import com.structurizr.model.Component
import nl.avisi.structurizr.site.generatr.normalize
import nl.avisi.structurizr.site.generatr.site.GeneratorContext

class SoftwareSystemContainerComponentDecisionPageViewModel(
    generatorContext: GeneratorContext, component: Component, decision: Decision
) : SoftwareSystemPageViewModel(generatorContext, component.container.softwareSystem, Tab.DECISIONS) {
    override val url = url(component, decision)

    val content = toHtml(this, transformADRLinks(decision.content, component), decision.format, generatorContext.svgFactory)

    private fun transformADRLinks(content: String, component: Component) =
        content.replace("\\[(.*)]\\(#(\\d+)\\)".toRegex()) {
            "[${it.groupValues[1]}](${url(component.container.softwareSystem, Tab.DECISIONS)}/${component.container.name.normalize()}/${component.name.normalize()}/${it.groupValues[2]})"
        }

    companion object {
        fun url(component: Component, decision: Decision) =
            "${url(component.container.softwareSystem, Tab.DECISIONS)}/${component.container.name.normalize()}/${component.name.normalize()}/${decision.id}"
    }
}
