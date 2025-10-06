package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.documentation.Decision
import com.structurizr.model.Component
import nl.avisi.structurizr.site.generatr.normalize
import nl.avisi.structurizr.site.generatr.site.GeneratorContext

class SoftwareSystemContainerComponentDecisionPageViewModel(
    generatorContext: GeneratorContext, component: Component, decision: Decision
) : SoftwareSystemPageViewModel(generatorContext, component.container.softwareSystem, Tab.DECISIONS) {
    override val url = url(component, decision)

    val content = toHtml(this, decision.content, decision.format, generatorContext.svgFactory)

    companion object {
        fun url(component: Component, decision: Decision) =
            "${url(component.container.softwareSystem, Tab.DECISIONS)}/${component.container.name.normalize()}/${component.name.normalize()}/${decision.id}"
    }
}
