package nl.avisi.structurizr.site.generatr.site.model.indexing

import com.structurizr.documentation.Documentation
import nl.avisi.structurizr.site.generatr.site.asUrlToDirectory
import nl.avisi.structurizr.site.generatr.site.model.PageViewModel
import nl.avisi.structurizr.site.generatr.site.model.contentText

fun workspaceDecisions(documentation: Documentation, viewModel: PageViewModel) = documentation.decisions
    .map { decision ->
        Document(
            "/decisions/${decision.id}".asUrlToDirectory(viewModel.url),
            "Workspace Decision",
            decision.title,
            "${decision.title} ${decision.contentText()}".trim()
        )
    }
