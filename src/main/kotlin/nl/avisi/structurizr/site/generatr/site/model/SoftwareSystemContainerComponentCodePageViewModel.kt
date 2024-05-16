package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.model.Container
import com.structurizr.model.Component
import nl.avisi.structurizr.site.generatr.normalize
import nl.avisi.structurizr.site.generatr.site.GeneratorContext

class SoftwareSystemContainerComponentCodePageViewModel(generatorContext: GeneratorContext, container: Container, component: Component) :
    SoftwareSystemPageViewModel(generatorContext, container.softwareSystem, Tab.CODE) {
    override val url = url(container, component)
    val images = generatorContext.workspace.views.imageViews
        .filter { it.element == component }
        .sortedBy { it.key }
        .map { ImageViewViewModel(it) }

    val visible = images.isNotEmpty()
    val containerTabs = createContainersCodeTabViewModel(generatorContext, container.softwareSystem)
    val componentTabs = createComponentsTabViewModel(generatorContext, container)

    companion object {
        fun url(container: Container, component: Component?) = "${url(container.softwareSystem, Tab.CODE)}/${container.name.normalize()}/${component?.name?.normalize()}"
    }
}
