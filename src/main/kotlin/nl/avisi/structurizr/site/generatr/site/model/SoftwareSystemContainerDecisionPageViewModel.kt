package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.documentation.Decision
import com.structurizr.model.SoftwareSystem
import com.structurizr.model.Container
import nl.avisi.structurizr.site.generatr.normalize
import nl.avisi.structurizr.site.generatr.site.GeneratorContext

class SoftwareSystemContainerDecisionPageViewModel(
    generatorContext: GeneratorContext, container: Container, decision: Decision
) : SoftwareSystemPageViewModel(generatorContext, container.softwareSystem, Tab.DECISIONS) {
    override val url = url(container, decision)

    val content = markdownToHtml(this, decision.content, generatorContext.svgFactory)

    companion object {
        fun url(container: Container, decision: Decision) =
            "${url(container.softwareSystem, Tab.DECISIONS)}/${container.name.normalize()}/${decision.id}"
    }
}
