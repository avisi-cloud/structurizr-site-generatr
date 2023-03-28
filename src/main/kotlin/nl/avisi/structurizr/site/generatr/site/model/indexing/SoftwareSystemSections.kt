package nl.avisi.structurizr.site.generatr.site.model.indexing

import com.structurizr.model.SoftwareSystem
import nl.avisi.structurizr.site.generatr.site.asUrlToDirectory
import nl.avisi.structurizr.site.generatr.site.model.PageViewModel
import nl.avisi.structurizr.site.generatr.site.model.SoftwareSystemPageViewModel
import nl.avisi.structurizr.site.generatr.site.model.contentText
import nl.avisi.structurizr.site.generatr.site.model.contentTitle

fun softwareSystemSections(softwareSystem: SoftwareSystem, viewModel: PageViewModel) = softwareSystem.documentation
    .sections
    .drop(1) // Drop software system home
    .map { section ->
        Document(
            "${
                SoftwareSystemPageViewModel.url(
                    softwareSystem,
                    SoftwareSystemPageViewModel.Tab.HOME
                )
            }/sections/${section.order}".asUrlToDirectory(viewModel.url),
            "Documentation",
            "${softwareSystem.name} | ${section.contentTitle()}",
            "${section.contentTitle()} ${section.contentText()}".trim()
        )
    }
