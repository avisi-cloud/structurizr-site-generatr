package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.model.SoftwareSystem
import nl.avisi.structurizr.site.generatr.site.GeneratorContext

class SoftwareSystemHomePageViewModel(generatorContext: GeneratorContext, softwareSystem: SoftwareSystem) :
    SoftwareSystemPageViewModel(generatorContext, softwareSystem, Tab.HOME) {
    val hasProperties = softwareSystem.properties.any()
    val propertiesTable = createPropertiesTableViewModel(softwareSystem.properties)
    val content = markdownToHtml(this, softwareSystem.info(), generatorContext.svgFactory)

    private fun SoftwareSystem.info() = documentation.sections
        .minByOrNull { it.order }
        ?.content
        ?: "# Description${System.lineSeparator()}${description}"
}
