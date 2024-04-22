package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.model.Container
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
        .filter { it.elementId in container.id }
        .sortedBy { it.key }
        .map { ImageViewViewModel(it) }

    val visible = container.hasComponents() or images.isNotEmpty()

    val containerTabs = createComponentsTabViewModel(generatorContext, container.softwareSystem)
    companion object {
        fun url(container: Container) = "${url(container.softwareSystem, Tab.COMPONENT)}/${container.name.normalize()}"
    }
}
