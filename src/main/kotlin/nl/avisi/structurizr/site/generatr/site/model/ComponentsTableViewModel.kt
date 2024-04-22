package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.model.SoftwareSystem
import nl.avisi.structurizr.site.generatr.site.GeneratorContext

fun SoftwareSystemPageViewModel.createComponentsTabViewModel(
    generatorContext: GeneratorContext,
    softwareSystem: SoftwareSystem,
) = buildList {
   softwareSystem
        .containers
        .filter { container ->
            container.hasComponents() or
                    generatorContext.workspace.views.imageViews.any { it.elementId in container.id }
        }
        .map {
            ComponentTabViewModel(
                this@createComponentsTabViewModel,
                it.name,
                SoftwareSystemContainerComponentsPageViewModel.url(it)
            )
        }
        .forEach {
            add(it)
        }
}
