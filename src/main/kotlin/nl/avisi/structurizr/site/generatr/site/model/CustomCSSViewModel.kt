package nl.avisi.structurizr.site.generatr.site.model

import nl.avisi.structurizr.site.generatr.site.GeneratorContext
import nl.avisi.structurizr.site.generatr.site.asUrlToFile

class CustomCSSViewModel(generatorContext: GeneratorContext, pageViewModel: PageViewModel) {
    val url = customCSSPath(generatorContext)?.let { "/$it".asUrlToFile(pageViewModel.url) }
    val type = extractType()
    val includecustomCSS = url != null

    private fun customCSSPath(generatorContext: GeneratorContext) =
        generatorContext.workspace.views.configuration.properties
            .getOrDefault(
                "generatr.style.customCSS",
                null
            )

    private fun extractType(): String {
        return if (url?.lowercase()?.startsWith("http") == true) "URI" else "FILE"
    }
}
