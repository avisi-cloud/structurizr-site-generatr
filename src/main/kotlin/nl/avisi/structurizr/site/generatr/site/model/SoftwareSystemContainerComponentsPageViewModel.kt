package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.model.Container
import nl.avisi.structurizr.site.generatr.includedProperties
import nl.avisi.structurizr.site.generatr.normalize
import nl.avisi.structurizr.site.generatr.site.GeneratorContext

class SoftwareSystemContainerComponentsPageViewModel(generatorContext: GeneratorContext, container: Container) :
    SoftwareSystemPageViewModel(generatorContext, container.softwareSystem, Tab.COMPONENT) {
    override val url = url(container)
    val diagrams = generatorContext.workspace.views.componentViews
        .filter { it.container == container }
        .sortedBy { it.key }
        .map { DiagramViewModel.forView(this, it, generatorContext.svgFactory) }
    val images = generatorContext.workspace.views.imageViews
        .filter { it.element == container }
        .sortedBy { it.key }
        .map { ImageViewViewModel(it) }

    val hasProperties = container.includedProperties.isNotEmpty()
    val propertiesTable = createPropertiesTableViewModel(container.includedProperties)
    val visible = diagrams.isNotEmpty() or images.isNotEmpty() or hasProperties
    val containerTabs = createContainersComponentTabViewModel(generatorContext, container.softwareSystem)
    val diagramIndexViewModel = DiagramIndexViewModel(
        diagrams,
        images
    )
    companion object {
        fun url(container: Container) = "${url(container.softwareSystem, Tab.COMPONENT)}/${container.name.normalize()}"
    }
}
