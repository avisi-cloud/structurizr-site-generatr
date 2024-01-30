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

    fun softwareSystemComponentsComponent(softwareSystem: SoftwareSystem, viewModel: PageViewModel) : List<Document> {

    var components = softwareSystem.containers
            .sortedBy { it.name }
            .flatMap { container -> container.components }

    var documents = emptyList<Document>().toMutableList()

    components.forEach {
        documents += Document(
                SoftwareSystemPageViewModel.url(softwareSystem, SoftwareSystemPageViewModel.Tab.COMPONENT)
                        .asUrlToDirectory(viewModel.url),
                "Component views",
                "${softwareSystem.name} | Component views | ${it.container.name}",
                it.name)
    }

    return documents
}
