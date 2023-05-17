package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.documentation.Decision
import com.structurizr.model.Container
import com.structurizr.model.SoftwareSystem
import nl.avisi.structurizr.site.generatr.hasContainerDecisions
import nl.avisi.structurizr.site.generatr.hasDecisions
import nl.avisi.structurizr.site.generatr.normalize
import nl.avisi.structurizr.site.generatr.site.GeneratorContext

class SoftwareSystemContainerDecisionsPageViewModel(generatorContext: GeneratorContext, container: Container) :
    SoftwareSystemPageViewModel(generatorContext, container.softwareSystem, Tab.DECISIONS) {
    override val url = url(container)
    val decisionsTable = createDecisionsTableViewModel(container.documentation.decisions) {
        "$url/${it.id}"
    }

    val visible = container.hasDecisions()
    val decisionTabs = createDecisionsTabViewModel(container.softwareSystem, Tab.DECISIONS)

    companion object {
        fun url(container: Container) =
                "${url(container.softwareSystem, Tab.DECISIONS)}/${container.name.normalize()}"
    }
}
