package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.model.Container
import nl.avisi.structurizr.site.generatr.hasImageViews
import nl.avisi.structurizr.site.generatr.site.GeneratorContext

fun SoftwareSystemPageViewModel.createComponentsTabViewModel(
    generatorContext: GeneratorContext,
    container: Container
) = buildList {
    container
        .components
        .sortedBy { it.name }
        .filter { component ->
            generatorContext.workspace.hasImageViews(component.id) }
        .map {
            ComponentTabViewModel(
                this@createComponentsTabViewModel,
                it.name,
                SoftwareSystemContainerComponentCodePageViewModel.url(it.container, it)
            )
        }
        .forEach { add(it) }
}
