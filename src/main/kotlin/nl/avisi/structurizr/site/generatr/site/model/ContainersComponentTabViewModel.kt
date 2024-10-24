package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.model.SoftwareSystem
import nl.avisi.structurizr.site.generatr.hasComponentDiagrams
import nl.avisi.structurizr.site.generatr.hasImageViews
import nl.avisi.structurizr.site.generatr.includedProperties
import nl.avisi.structurizr.site.generatr.site.GeneratorContext

fun SoftwareSystemPageViewModel.createContainersComponentTabViewModel(
    generatorContext: GeneratorContext,
    softwareSystem: SoftwareSystem,
) = buildList {
   softwareSystem
        .containers
        .sortedBy { it.name }
        .filter { container ->
            container.includedProperties.isNotEmpty() or
                    generatorContext.workspace.hasComponentDiagrams(container) or
                    generatorContext.workspace.hasImageViews(container.id) }
        .map {
            ContainerTabViewModel(
                this@createContainersComponentTabViewModel,
                it.name,
                SoftwareSystemContainerComponentsPageViewModel.url(it)
            )
        }
        .forEach { add(it) }
}
