package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.documentation.Decision
import com.structurizr.model.Component
import com.structurizr.model.Container
import nl.avisi.structurizr.site.generatr.hasComponentDecisions
import nl.avisi.structurizr.site.generatr.hasDecisions
import nl.avisi.structurizr.site.generatr.normalize
import nl.avisi.structurizr.site.generatr.site.GeneratorContext

abstract class BaseSoftwareSystemContainerDecisionsPageViewModel(
    generatorContext: GeneratorContext,
    container: Container
) : BaseSoftwareSystemDecisionsPageViewModel(
    generatorContext,
    container.softwareSystem
) {
    val componentDecisionsTabs by lazy {
        val components = container.components.filter { it.hasDecisions() }
        buildList(components.size) {
            if (container.hasDecisions())
                add(
                    DecisionTabViewModel(
                        this@BaseSoftwareSystemContainerDecisionsPageViewModel,
                        "Container",
                        decisionTableItemUrl(container),
                        Match.EXACT
                    )
                )
            addAll(
                components.map { component ->
                    DecisionTabViewModel(
                        this@BaseSoftwareSystemContainerDecisionsPageViewModel,
                        component.name,
                        componentDecisionItemUrl(component)
                    )
                }
            )
        }
    }

    abstract fun decisionTableItemUrl(container: Container, decision: Decision? = null): String
    abstract fun componentDecisionItemUrl(component: Component): String
}

class SoftwareSystemContainerDecisionsPageViewModel(generatorContext: GeneratorContext, container: Container) :
    BaseSoftwareSystemContainerDecisionsPageViewModel(generatorContext, container) {
    override val url = url(container)
    val decisionsTable = createDecisionsTableViewModel(container.documentation.decisions) {
        "$url/${it.id}"
    }

    private val componentDecisionsVisible = container.hasComponentDecisions()
    val containerDecisionsVisible = container.hasDecisions()

    val visible = componentDecisionsVisible or containerDecisionsVisible
    val onlyComponentDecisionsVisible = !containerDecisionsVisible and componentDecisionsVisible

    override fun decisionTableItemUrl(container: Container, decision: Decision?) = decision?.let { "${url(container)}/${decision.id}" } ?: url(container)
    override fun componentDecisionItemUrl(component: Component) = "${url(component.container)}/${component.name.normalize()}"

    companion object {
        fun url(container: Container) = "${url(container.softwareSystem, Tab.DECISIONS)}/${container.name.normalize()}"
    }
}
