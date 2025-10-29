package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.model.Container
import com.structurizr.model.SoftwareSystem
import nl.avisi.structurizr.site.generatr.hasContainerDecisions
import nl.avisi.structurizr.site.generatr.hasDecisions
import nl.avisi.structurizr.site.generatr.site.GeneratorContext

abstract class BaseSoftwareSystemDecisionsPageViewModel(
    generatorContext: GeneratorContext,
    softwareSystem: SoftwareSystem
) : SoftwareSystemPageViewModel(generatorContext, softwareSystem, Tab.DECISIONS) {
    val decisionTabs = createDecisionsTabViewModel(softwareSystem, Tab.DECISIONS) {
        if (it is Container) Match.CHILD else Match.EXACT
    }
}

class SoftwareSystemDecisionsPageViewModel(generatorContext: GeneratorContext, softwareSystem: SoftwareSystem) :
    BaseSoftwareSystemDecisionsPageViewModel(generatorContext, softwareSystem) {

    val decisionsTable = createDecisionsTableViewModel(softwareSystem.documentation.decisions) {
        "$url/${it.id}"
    }

    private val containerDecisionsVisible = softwareSystem.hasContainerDecisions(recursive = true)
    val softwareSystemDecisionsVisible = softwareSystem.hasDecisions()

    val visible = softwareSystemDecisionsVisible or containerDecisionsVisible
    val onlyContainersDecisionsVisible = !softwareSystemDecisionsVisible and containerDecisionsVisible
}

