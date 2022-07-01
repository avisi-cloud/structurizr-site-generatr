package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.model.Location
import nl.avisi.structurizr.site.generatr.normalize
import nl.avisi.structurizr.site.generatr.site.context.GeneratorContext

class MenuViewModel(generatorContext: GeneratorContext, private val pageViewModel: PageViewModel) {
    val generalItems = sequence {
        yield(createMenuItem("Home", "/${generatorContext.currentBranch}"))

        if (generatorContext.workspace.documentation.decisions.isNotEmpty())
            yield(createMenuItem("Decisions", "/${generatorContext.currentBranch}/decisions"))

        yield(createMenuItem("Software systems", "/${generatorContext.currentBranch}/software-systems"))

        generatorContext.workspace.documentation.sections
            .sortedBy { it.order }
            .drop(1)
            .forEach {
                yield(createMenuItem(it.title, "/${generatorContext.currentBranch}/${it.title.normalize()}"))
            }
    }.toList()

    val softwareSystemItems = generatorContext.workspace.model.softwareSystems
        .filter { it.location == Location.Internal }
        .sortedBy { it.name }
        .map { createMenuItem(it.name, "/${generatorContext.currentBranch}/${it.name.normalize()}") }

    private fun createMenuItem(title: String, href: String) =
        LinkViewModel(pageViewModel, title, href, href == pageViewModel.url)
}
