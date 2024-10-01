package nl.avisi.structurizr.site.generatr.site.model.indexing

import com.structurizr.model.Container
import nl.avisi.structurizr.site.generatr.site.asUrlToDirectory
import nl.avisi.structurizr.site.generatr.site.model.PageViewModel
import nl.avisi.structurizr.site.generatr.site.model.SoftwareSystemContainerDecisionPageViewModel
import nl.avisi.structurizr.site.generatr.site.model.contentText

fun softwareSystemContainerDecisions(container: Container, viewModel: PageViewModel) = container
    .documentation
    .decisions
    .map { decision ->
        Document(
            SoftwareSystemContainerDecisionPageViewModel.url(
                container,
                decision
            ).asUrlToDirectory(viewModel.url),
            "Container Decision",
            "${container.name} | ${decision.title}",
            "${decision.title} ${decision.contentText()}".trim()
        )
    }
