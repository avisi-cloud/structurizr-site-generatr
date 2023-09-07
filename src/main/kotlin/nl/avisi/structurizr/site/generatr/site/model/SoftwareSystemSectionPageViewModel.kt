package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.documentation.Section
import com.structurizr.model.SoftwareSystem
import nl.avisi.structurizr.site.generatr.site.GeneratorContext

class SoftwareSystemSectionPageViewModel(
    generatorContext: GeneratorContext, softwareSystem: SoftwareSystem, section: Section
) : SoftwareSystemPageViewModel(generatorContext, softwareSystem, Tab.SECTIONS) {
    override val url = url(softwareSystem, section)

    val content = toHtml(this, section.content, section.format, generatorContext.svgFactory)

    companion object {
        fun url(softwareSystem: SoftwareSystem, section: Section) =
            "${url(softwareSystem, Tab.SECTIONS)}/${section.order}"
    }
}
