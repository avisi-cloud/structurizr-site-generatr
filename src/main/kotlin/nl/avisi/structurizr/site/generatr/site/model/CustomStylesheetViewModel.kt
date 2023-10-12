package nl.avisi.structurizr.site.generatr.site.model

import nl.avisi.structurizr.site.generatr.site.GeneratorContext

class CustomStylesheetViewModel(generatorContext: GeneratorContext) {
    val resourceURI = getResourceURI(generatorContext)
    val includeCustomStylesheet = resourceURI != null

    private fun getResourceURI(generatorContext: GeneratorContext): String? {
        val stylesheet = generatorContext.workspace.views.configuration.properties
            .getOrDefault(
            "generatr.style.customStylesheet",
            null
        )

        if (stylesheet != null) {
            return if (stylesheet.lowercase().startsWith("http")) stylesheet else "./$stylesheet"
        }

        return null
    }

}
