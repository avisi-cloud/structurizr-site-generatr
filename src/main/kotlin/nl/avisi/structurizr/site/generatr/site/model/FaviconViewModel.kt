package nl.avisi.structurizr.site.generatr.site.model

import nl.avisi.structurizr.site.generatr.site.GeneratorContext
import nl.avisi.structurizr.site.generatr.site.asUrlToFile
import java.io.File

class FaviconViewModel(generatorContext: GeneratorContext, pageViewModel: PageViewModel) {
    val url = faviconPath(generatorContext)?.let { "/$it".asUrlToFile(pageViewModel.url) }
    val type = extractType()
    val includeFavicon = url != null

    private fun faviconPath(generatorContext: GeneratorContext) =
        generatorContext.workspace.views.configuration.properties
            .getOrDefault(
                "structurizr.style.favicon.path",
                null
            )

    private fun extractType(): String? {
        return url?.let {
            val extension = File(it).extension.lowercase()
            if (extension.isBlank() || !arrayOf("ico", "png", "gif").contains(extension))
                throw IllegalArgumentException("Favicon must be a valid *.ico, *.png of *.gif file")

            "image/$extension"
        }
    }
}
