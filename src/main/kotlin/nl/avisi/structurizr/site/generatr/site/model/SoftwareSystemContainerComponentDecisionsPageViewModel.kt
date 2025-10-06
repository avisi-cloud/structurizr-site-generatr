package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.documentation.Decision
import com.structurizr.model.Component
import com.structurizr.model.Container
import nl.avisi.structurizr.site.generatr.hasDecisions
import nl.avisi.structurizr.site.generatr.normalize
import nl.avisi.structurizr.site.generatr.site.GeneratorContext

class SoftwareSystemContainerComponentDecisionsPageViewModel(
    generatorContext: GeneratorContext,
    component: Component
) : BaseSoftwareSystemContainerDecisionsPageViewModel(
    generatorContext,
    component.container
) {
    val visible = component.hasDecisions()

    override val url = url(component)
    val decisionsTable = createDecisionsTableViewModel(component.documentation.decisions) {
        "$url/${it.id}"
    }

    override fun decisionTableItemUrl(container: Container, decision: Decision?) = decision?.let { "${url(container)}/${decision.id}" } ?: url(container)
    override fun componentDecisionItemUrl(component: Component) = "${url(component.container)}/${component.name.normalize()}"

    companion object {
        fun url(container: Container) = "${url(container.softwareSystem, Tab.DECISIONS)}/${container.name.normalize()}"
        fun url(component: Component) = "${url(component.container.softwareSystem, Tab.DECISIONS)}/${component.container.name.normalize()}/${component.name.normalize()}"
    }
}
