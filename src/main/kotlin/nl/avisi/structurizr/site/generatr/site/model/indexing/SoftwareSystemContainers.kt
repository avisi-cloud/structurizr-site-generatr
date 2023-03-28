package nl.avisi.structurizr.site.generatr.site.model.indexing

import com.structurizr.model.SoftwareSystem
import nl.avisi.structurizr.site.generatr.site.asUrlToDirectory
import nl.avisi.structurizr.site.generatr.site.model.PageViewModel
import nl.avisi.structurizr.site.generatr.site.model.SoftwareSystemPageViewModel

fun softwareSystemContainers(softwareSystem: SoftwareSystem, viewModel: PageViewModel) = softwareSystem.containers
    .sortedBy { it.name }
    .flatMap { container ->
        listOf(container.name, container.description, container.technology)
    }
    .filter { it != null && it.isNotBlank() }
    .joinToString(" ")
    .ifBlank { null }
    ?.let {
        Document(
            SoftwareSystemPageViewModel.url(softwareSystem, SoftwareSystemPageViewModel.Tab.CONTAINER)
                .asUrlToDirectory(viewModel.url),
            "Container views",
            "${softwareSystem.name} | Container views",
            it
        )
    }
