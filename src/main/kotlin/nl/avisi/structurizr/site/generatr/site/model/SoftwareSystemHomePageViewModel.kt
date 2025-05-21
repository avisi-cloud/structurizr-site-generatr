package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.documentation.Format
import com.structurizr.model.SoftwareSystem
import nl.avisi.structurizr.site.generatr.includedProperties
import nl.avisi.structurizr.site.generatr.site.GeneratorContext

class SoftwareSystemHomePageViewModel(generatorContext: GeneratorContext, softwareSystem: SoftwareSystem) :
    SoftwareSystemPageViewModel(generatorContext, softwareSystem, Tab.HOME) {
    val hasProperties = softwareSystem.includedProperties.any()
    val propertiesTable = createPropertiesTableViewModel(softwareSystem.includedProperties)
    val content = toHtml(this, softwareSystem.info(), softwareSystem.format(), generatorContext.svgFactory)

    private fun SoftwareSystem.info() = documentation.sections
        .minByOrNull { it.order }
        ?.content
        ?: "### Description${System.lineSeparator()}${description}"

    private fun SoftwareSystem.format() = documentation.sections
        .minByOrNull { it.order }
        ?.format
        ?: Format.Markdown
}
