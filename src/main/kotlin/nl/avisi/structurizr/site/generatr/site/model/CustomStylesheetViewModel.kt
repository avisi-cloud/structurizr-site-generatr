package nl.avisi.structurizr.site.generatr.site.model

import nl.avisi.structurizr.site.generatr.site.GeneratorContext
import nl.avisi.structurizr.site.generatr.site.asUrlToFile

class CustomStylesheetViewModel(generatorContext: GeneratorContext, pageViewModel: PageViewModel) {
    val url = customStylesheetPath(generatorContext)?.let { "/$it".asUrlToFile(pageViewModel.url) }
    val type = extractType()
    val includeCustomStylesheet = url != null

    private fun customStylesheetPath(generatorContext: GeneratorContext) =
        generatorContext.workspace.views.configuration.properties
            .getOrDefault(
                "generatr.style.customStylesheet",
                null
            )

    private fun extractType(): String {
        return if (url?.lowercase()?.startsWith("http") == true) "URI" else "FILE"
    }
}
