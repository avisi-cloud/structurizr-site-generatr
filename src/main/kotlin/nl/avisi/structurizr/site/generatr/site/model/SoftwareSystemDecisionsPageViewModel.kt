package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.model.SoftwareSystem
import nl.avisi.structurizr.site.generatr.hasContainerDecisions
import nl.avisi.structurizr.site.generatr.hasDecisions
import nl.avisi.structurizr.site.generatr.site.GeneratorContext

class SoftwareSystemDecisionsPageViewModel(generatorContext: GeneratorContext, softwareSystem: SoftwareSystem) :
    SoftwareSystemPageViewModel(generatorContext, softwareSystem, Tab.DECISIONS) {

    val decisionsTable = createDecisionsTableViewModel(softwareSystem.documentation.decisions) {
        "$url/${it.id}"
    }

    private val containerDecisionsVisible = softwareSystem.hasContainerDecisions()
    val softwareSystemDecisionsVisible = softwareSystem.hasDecisions()

    val visible = softwareSystemDecisionsVisible or containerDecisionsVisible
    val onlyContainersDecisionsVisible = !softwareSystemDecisionsVisible and containerDecisionsVisible

    val decisionTabs = createDecisionsTabViewModel(softwareSystem, Tab.DECISIONS)
}
