package nl.avisi.structurizr.site.generatr.site.model.indexing

import com.structurizr.model.SoftwareSystem
import nl.avisi.structurizr.site.generatr.site.asUrlToDirectory
import nl.avisi.structurizr.site.generatr.site.model.PageViewModel
import nl.avisi.structurizr.site.generatr.site.model.SoftwareSystemPageViewModel

fun softwareSystemContext(softwareSystem: SoftwareSystem, viewModel: PageViewModel) = Document(
    SoftwareSystemPageViewModel.url(softwareSystem, SoftwareSystemPageViewModel.Tab.SYSTEM_CONTEXT)
        .asUrlToDirectory(viewModel.url),
    "Context views",
    "${softwareSystem.name} | Context views",
    "${softwareSystem.name} ${softwareSystem.description}".trim()
)
