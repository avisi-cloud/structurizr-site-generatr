package nl.avisi.structurizr.site.generatr.site.model.indexing

import com.structurizr.documentation.Documentation
import nl.avisi.structurizr.site.generatr.normalize
import nl.avisi.structurizr.site.generatr.site.asUrlToDirectory
import nl.avisi.structurizr.site.generatr.site.model.PageViewModel
import nl.avisi.structurizr.site.generatr.site.model.contentText
import nl.avisi.structurizr.site.generatr.site.model.contentTitle

fun workspaceSections(documentation: Documentation, viewModel: PageViewModel) = documentation.sections
    .drop(1) // Drop home
    .map { section ->
        Document(
            "/${section.contentTitle().normalize()}".asUrlToDirectory(viewModel.url),
            "Workspace Documentation",
            section.contentTitle(),
            "${section.contentTitle()} ${section.contentText()}".trim()
        )
    }
