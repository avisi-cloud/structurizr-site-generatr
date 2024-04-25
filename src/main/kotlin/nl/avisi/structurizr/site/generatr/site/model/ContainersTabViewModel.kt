package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.model.SoftwareSystem
import nl.avisi.structurizr.site.generatr.hasComponentDiagrams
import nl.avisi.structurizr.site.generatr.hasImageViews
import nl.avisi.structurizr.site.generatr.site.GeneratorContext

fun SoftwareSystemPageViewModel.createContainersTabViewModel(
    generatorContext: GeneratorContext,
    softwareSystem: SoftwareSystem,
) = buildList {
   softwareSystem
        .containers
        .filter { container ->
            generatorContext.workspace.hasComponentDiagrams(container) or
                    generatorContext.workspace.hasImageViews(container.id) }
        .map {
            ContainerTabViewModel(
                this@createContainersTabViewModel,
                it.name,
                SoftwareSystemContainerComponentsPageViewModel.url(it)
            )
        }
        .forEach { add(it) }
}
