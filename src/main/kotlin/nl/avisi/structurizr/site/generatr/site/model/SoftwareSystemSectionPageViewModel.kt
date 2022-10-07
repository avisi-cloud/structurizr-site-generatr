package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.documentation.Section
import com.structurizr.model.SoftwareSystem
import nl.avisi.structurizr.site.generatr.site.GeneratorContext

class SoftwareSystemSectionPageViewModel(
    generatorContext: GeneratorContext, softwareSystem: SoftwareSystem, section: Section
) : SoftwareSystemPageViewModel(generatorContext, softwareSystem, Tab.DECISIONS) {
    override val url = url(softwareSystem, section)

    val markdown = MarkdownViewModel(section.content)

    companion object {
        fun url(softwareSystem: SoftwareSystem, section: Section) =
            "${url(softwareSystem, Tab.SECTIONS)}/${section.order}"
    }
}
