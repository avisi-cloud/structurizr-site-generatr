package nl.avisi.structurizr.site.generatr.site.model.indexing

import com.structurizr.model.SoftwareSystem
import nl.avisi.structurizr.site.generatr.site.asUrlToDirectory
import nl.avisi.structurizr.site.generatr.site.model.PageViewModel
import nl.avisi.structurizr.site.generatr.site.model.SoftwareSystemPageViewModel

fun softwareSystemRelationships(softwareSystem: SoftwareSystem, viewModel: PageViewModel) = softwareSystem.relationships
    .filter { r -> r.source == softwareSystem }
    .sortedBy { it.destination.name }
    .flatMap { relationship ->
        listOf(relationship.destination.name, relationship.description, relationship.technology)
    }
    .plus(
        softwareSystem.model.softwareSystems
            .sortedBy { it.name }
            .filterNot { system -> system == softwareSystem }
            .flatMap { other ->
                other.relationships
                    .filter { r -> r.destination == softwareSystem }
                    .sortedBy { it.source.name }
                    .flatMap { relationship ->
                        listOf(relationship.source.name, relationship.description, relationship.technology)
                    }
            }
    )
    .filter { it != null && it.isNotBlank() }
    .joinToString(" ")
    .ifBlank { null }
    ?.let {
        Document(
            SoftwareSystemPageViewModel.url(softwareSystem, SoftwareSystemPageViewModel.Tab.DEPENDENCIES)
                .asUrlToDirectory(viewModel.url),
            "Dependencies",
            "${softwareSystem.name} | Dependencies",
            it
        )
    }
