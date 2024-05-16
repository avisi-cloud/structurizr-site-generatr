package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.model.SoftwareSystem
import nl.avisi.structurizr.site.generatr.hasComponentDiagrams
import nl.avisi.structurizr.site.generatr.hasImageViews
import nl.avisi.structurizr.site.generatr.site.GeneratorContext

fun SoftwareSystemPageViewModel.createContainersCodeTabViewModel(
    generatorContext: GeneratorContext,
    softwareSystem: SoftwareSystem,
) = buildList {
    softwareSystem
        .containers
        .sortedBy { it.name }
        .filter { container ->
            (generatorContext.workspace.hasComponentDiagrams(container) or
                    generatorContext.workspace.hasImageViews(container.id)) and
                    container.components.any { component -> generatorContext.workspace.hasImageViews(component.id) }
        }
        .map { container ->
            ContainerTabViewModel(
                this@createContainersCodeTabViewModel,
                container.name,
                SoftwareSystemContainerComponentCodePageViewModel.url(container, container.components.sortedBy { it.name }.firstOrNull { component -> generatorContext.workspace.hasImageViews(component.id) }),
                Match.SIBLING
            )
        }
        .forEach { add(it) }
}
