package nl.avisi.structurizr.site.generatr.site.context

import com.structurizr.documentation.Decision
import com.structurizr.model.SoftwareSystem
import nl.avisi.structurizr.site.generatr.normalizedName

class SoftwareSystemDecisionPageContext(
    generatorContext: GeneratorContext,
    override val softwareSystem: SoftwareSystem,
    val decision: Decision
) :
    AbstractSoftwareSystemPageContext(
        generatorContext,
        softwareSystem.name,
        "${softwareSystem.normalizedName}/decisions/${decision.id}/index.html"
    )
