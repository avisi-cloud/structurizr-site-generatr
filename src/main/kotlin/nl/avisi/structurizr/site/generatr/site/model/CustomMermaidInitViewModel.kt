package nl.avisi.structurizr.site.generatr.site.model

import nl.avisi.structurizr.site.generatr.site.GeneratorContext
import nl.avisi.structurizr.site.generatr.site.asUrlToFile

class CustomMermaidInitViewModel(generatorContext: GeneratorContext, pageViewModel: PageViewModel) {
    val resourceURI = getResourceURI(generatorContext)?.let {
        if (!it.lowercase().startsWith("http"))
            "/$it".asUrlToFile(pageViewModel.url)
        else
            it
    }
    val includeCustomInit = resourceURI != null

    private fun getResourceURI(generatorContext: GeneratorContext) =
        generatorContext.workspace.views.configuration.properties
            .getOrDefault(
                "generatr.mermaid.customInit",
                null
            )
}
