package nl.avisi.structurizr.site.generatr.site.model.indexing

import com.structurizr.documentation.Documentation
import nl.avisi.structurizr.site.generatr.site.asUrlToDirectory
import nl.avisi.structurizr.site.generatr.site.model.HomePageViewModel
import nl.avisi.structurizr.site.generatr.site.model.PageViewModel
import nl.avisi.structurizr.site.generatr.site.model.contentText
import nl.avisi.structurizr.site.generatr.site.model.contentTitle

fun home(documentation: Documentation, viewModel: PageViewModel) = documentation.sections.firstOrNull()
    ?.let { section ->
        Document(
            HomePageViewModel.url().asUrlToDirectory(viewModel.url),
            "Home",
            section.contentTitle(),
            "${section.contentTitle()} ${section.contentText()}".trim()
        )
    }
