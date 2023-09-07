package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.documentation.Section
import com.structurizr.model.Container
import nl.avisi.structurizr.site.generatr.normalize
import nl.avisi.structurizr.site.generatr.site.GeneratorContext

class SoftwareSystemContainerSectionPageViewModel(
    generatorContext: GeneratorContext,
    container: Container,
    section: Section
) : SoftwareSystemPageViewModel(generatorContext, container.softwareSystem, Tab.SECTIONS) {
    override val url = url(container, section)

    val content = toHtml(this, section.content, section.format, generatorContext.svgFactory)

    companion object {
        fun url(container: Container, section: Section) =
            "${url(container.softwareSystem, Tab.SECTIONS)}/${container.name.normalize()}/${section.order}"
    }
}
