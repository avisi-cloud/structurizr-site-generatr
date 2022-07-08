package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.model.Location
import nl.avisi.structurizr.site.generatr.site.context.GeneratorContext

class MenuViewModel(generatorContext: GeneratorContext, private val pageViewModel: PageViewModel) {
    val generalItems = sequence {
        yield(createMenuItem("Home", HomePageViewModel.url()))

        if (generatorContext.workspace.documentation.decisions.isNotEmpty())
            yield(createMenuItem("Decisions", WorkspaceDecisionsPageViewModel.url()))

        yield(createMenuItem("Software systems", SoftwareSystemsPageViewModel.url()))

        generatorContext.workspace.documentation.sections
            .sortedBy { it.order }
            .drop(1)
            .forEach {
                yield(createMenuItem(it.title, WorkspaceDocumentationSectionPageViewModel.url(it)))
            }
    }.toList()

    val softwareSystemItems = generatorContext.workspace.model.softwareSystems
        .filter { it.location == Location.Internal }
        .sortedBy { it.name }
        .map { createMenuItem(it.name, SoftwareSystemHomePageViewModel.url(it)) }

    private fun createMenuItem(title: String, href: String) =
        LinkViewModel(pageViewModel, title, href, href == pageViewModel.url)
}
