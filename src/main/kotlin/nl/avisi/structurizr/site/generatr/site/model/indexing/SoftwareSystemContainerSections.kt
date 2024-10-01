package nl.avisi.structurizr.site.generatr.site.model.indexing

import com.structurizr.model.Container
import nl.avisi.structurizr.site.generatr.site.asUrlToDirectory
import nl.avisi.structurizr.site.generatr.site.model.*

fun softwareSystemContainerSections(container: Container, viewModel: PageViewModel) = container
    .documentation
    .sections
    .map { section ->
        Document(
            SoftwareSystemContainerSectionPageViewModel.url(
                container,
                section
            ).asUrlToDirectory(viewModel.url),
            "Container Documentation",
            "${container.name} | ${section.contentTitle()}",
            "${section.contentTitle()} ${section.contentText()}".trim()
        )
    }
