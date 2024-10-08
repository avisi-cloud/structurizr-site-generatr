package nl.avisi.structurizr.site.generatr.site.model.indexing

import com.structurizr.model.SoftwareSystem
import nl.avisi.structurizr.site.generatr.site.asUrlToDirectory
import nl.avisi.structurizr.site.generatr.site.model.PageViewModel
import nl.avisi.structurizr.site.generatr.site.model.SoftwareSystemDecisionPageViewModel
import nl.avisi.structurizr.site.generatr.site.model.contentText

fun softwareSystemDecisions(softwareSystem: SoftwareSystem, viewModel: PageViewModel) = softwareSystem.documentation
    .decisions
    .map { decision ->
        Document(
            SoftwareSystemDecisionPageViewModel.url(
                softwareSystem,
                decision
            ).asUrlToDirectory(viewModel.url),
            "Software System Decision",
            "${softwareSystem.name} | ${decision.title}",
            "${decision.title} ${decision.contentText()}".trim()
        )
    }
