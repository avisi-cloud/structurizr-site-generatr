package nl.avisi.structurizr.site.generatr.site.model.indexing

import com.structurizr.model.SoftwareSystem
import nl.avisi.structurizr.site.generatr.site.asUrlToDirectory
import nl.avisi.structurizr.site.generatr.site.model.PageViewModel
import nl.avisi.structurizr.site.generatr.site.model.SoftwareSystemPageViewModel

fun softwareSystemComponents(softwareSystem: SoftwareSystem, viewModel: PageViewModel) = softwareSystem.containers
    .sortedBy { it.name }
    .flatMap { container ->
        container.components
            .sortedBy { it.name }
            .flatMap { component ->
                listOf(component.name, component.description, component.technology)
            }
    }
    .filter { it != null && it.isNotBlank() }
    .joinToString(" ")
    .ifBlank { null }
    ?.let {
        Document(
            SoftwareSystemPageViewModel.url(softwareSystem, SoftwareSystemPageViewModel.Tab.COMPONENT)
                .asUrlToDirectory(viewModel.url),
            "Component views",
            "${softwareSystem.name} | Component views",
            it
        )
    }
