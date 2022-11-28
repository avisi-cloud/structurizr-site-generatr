package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.model.SoftwareSystem
import nl.avisi.structurizr.site.generatr.hasDecisions
import nl.avisi.structurizr.site.generatr.site.GeneratorContext

class SoftwareSystemDecisionsPageViewModel(generatorContext: GeneratorContext, softwareSystem: SoftwareSystem) :
    SoftwareSystemPageViewModel(generatorContext, softwareSystem, Tab.DECISIONS) {
    val decisionsTable = createDecisionsTableViewModel(softwareSystem.documentation.decisions) {
        "$url/${it.id}"
    }
    val visible = softwareSystem.hasDecisions()
}
