package nl.avisi.structurizr.site.generatr.site.model.indexing

import com.structurizr.model.SoftwareSystem
import nl.avisi.structurizr.site.generatr.site.asUrlToDirectory
import nl.avisi.structurizr.site.generatr.site.model.PageViewModel
import nl.avisi.structurizr.site.generatr.site.model.SoftwareSystemPageViewModel
import nl.avisi.structurizr.site.generatr.site.model.contentText

fun softwareSystemDecisions(softwareSystem: SoftwareSystem, viewModel: PageViewModel) = softwareSystem.documentation
    .decisions
    .map { decision ->
        Document(
            "${
                SoftwareSystemPageViewModel.url(
                    softwareSystem,
                    SoftwareSystemPageViewModel.Tab.HOME
                )
            }/decisions/${decision.id}".asUrlToDirectory(viewModel.url),
            "Decision",
            "${softwareSystem.name} | ${decision.title}",
            "${decision.title} ${decision.contentText()}".trim()
        )
    }
