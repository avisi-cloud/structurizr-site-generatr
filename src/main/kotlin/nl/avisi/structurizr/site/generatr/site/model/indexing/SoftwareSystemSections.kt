package nl.avisi.structurizr.site.generatr.site.model.indexing

import com.structurizr.model.SoftwareSystem
import nl.avisi.structurizr.site.generatr.site.asUrlToDirectory
import nl.avisi.structurizr.site.generatr.site.model.*

fun softwareSystemSections(softwareSystem: SoftwareSystem, viewModel: PageViewModel) = softwareSystem.documentation
    .sections
    .drop(1) // Drop software system home
    .map { section ->
        Document(

            SoftwareSystemSectionPageViewModel.url(
                softwareSystem,
                section
            ).asUrlToDirectory(viewModel.url),
            "Software System Documentation",
            "${softwareSystem.name} | ${section.contentTitle()}",
            "${section.contentTitle()} ${section.contentText()}".trim()
        )
    }
