package nl.avisi.structurizr.site.generatr.site.model

import nl.avisi.structurizr.site.generatr.internalSoftwareSystems
import nl.avisi.structurizr.site.generatr.site.GeneratorContext

class MenuViewModel(generatorContext: GeneratorContext, private val pageViewModel: PageViewModel) {
    val generalItems = sequence {
        yield(createMenuItem("Home", HomePageViewModel.url()))

        if (generatorContext.workspace.documentation.decisions.isNotEmpty())
            yield(createMenuItem("Decisions", WorkspaceDecisionsPageViewModel.url(), false))

        yield(createMenuItem("Software Systems", SoftwareSystemsPageViewModel.url()))

        generatorContext.workspace.documentation.sections
            .sortedBy { it.order }
            .drop(1)
            .forEach { yield(createMenuItem(it.title, WorkspaceDocumentationSectionPageViewModel.url(it))) }
    }.toList()

    val softwareSystemItems = generatorContext.workspace.model.internalSoftwareSystems
        .sortedBy { it.name.lowercase() }
        .map {
            createMenuItem(it.name, SoftwareSystemPageViewModel.url(it, SoftwareSystemPageViewModel.Tab.HOME), false)
        }

    private fun createMenuItem(title: String, href: String, exact: Boolean = true) =
        LinkViewModel(pageViewModel, title, href, exact)
}
