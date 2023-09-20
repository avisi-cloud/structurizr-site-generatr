package nl.avisi.structurizr.site.generatr.site.model

import nl.avisi.structurizr.site.generatr.site.GeneratorContext
import nl.avisi.structurizr.site.generatr.site.asUrlToFile
import java.io.File

class CustomCSSViewModel(generatorContext: GeneratorContext, pageViewModel: PageViewModel) {
    val url = customCSSPath(generatorContext)?.let { "/$it".asUrlToFile(pageViewModel.url) }
    val type = extractType()
    val includecustomCSS = url != null

    private fun customCSSPath(generatorContext: GeneratorContext) =
        generatorContext.workspace.views.configuration.properties
            .getOrDefault(
                "generatr.style.customCSSPath",
                null
            )

    private fun extractType(): String? {
        return url?.let {
            val extension = File(it).extension.lowercase()
            if (extension.isBlank() || !arrayOf("css").contains(extension))
                throw IllegalArgumentException("Custom CSS must be a valid *.css file")

            "image/$extension"
        }
    }
}
