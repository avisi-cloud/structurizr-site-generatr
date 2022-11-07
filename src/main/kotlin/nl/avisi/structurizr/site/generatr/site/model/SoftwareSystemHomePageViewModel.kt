package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.model.SoftwareSystem
import nl.avisi.structurizr.site.generatr.site.GeneratorContext

class SoftwareSystemHomePageViewModel(generatorContext: GeneratorContext, softwareSystem: SoftwareSystem) :
    SoftwareSystemPageViewModel(generatorContext, softwareSystem, Tab.HOME) {
    val hasProperties = softwareSystem.properties.any()
    val propertiesTable = createPropertiesTableViewModel(softwareSystem.properties)
    val content = softwareSystem.documentation.sections
        .minByOrNull { it.order }
        ?.let { MarkdownViewModel(it.content, generatorContext.svgFactory) }
        ?: MarkdownViewModel(
            "# Description${System.lineSeparator()}${softwareSystem.description}",
            generatorContext.svgFactory
        )
}
