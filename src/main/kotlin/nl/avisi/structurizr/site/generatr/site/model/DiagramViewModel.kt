package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.view.View
import nl.avisi.structurizr.site.generatr.site.ExporterType
import nl.avisi.structurizr.site.generatr.site.exporterType

data class DiagramViewModel(
    val key: String,
    val title: String,
    val description: String?,
    val svg: String?,
    val type: ExporterType,
    val diagramWidthInPixels: Int?,
    val svgLocation: ImageViewModel,
    val pngLocation: ImageViewModel,
    val pumlLocation: ImageViewModel,
    val d2Localtion: ImageViewModel,
) {
    companion object {
        fun forView(pageViewModel: PageViewModel, view: View, svgFactory: (key: String, url: String) -> String?) =
            forView(pageViewModel, view.key, view.name, view.title, view.description.ifBlank { null }, svgFactory)

        fun forView(
            pageViewModel: PageViewModel,
            key: String,
            name: String,
            title: String?,
            description: String?,
            svgFactory: (key: String, url: String) -> String?
        ): DiagramViewModel {
            val svg = svgFactory(key, pageViewModel.url)
            return DiagramViewModel(
                key,
                title ?: name,
                description,
                svg,
                pageViewModel.generatorContext.workspace.exporterType(),
                extractDiagramWidthInPixels(if (pageViewModel.generatorContext.workspace.exporterType() == ExporterType.D2) null else svg),
                ImageViewModel(pageViewModel, "/svg/$key.svg"),
                ImageViewModel(pageViewModel, "/png/$key.png"),
                ImageViewModel(pageViewModel, "/puml/$key.puml"),
                ImageViewModel(pageViewModel, "/d2/$key.d2")
            )
        }

        private fun extractDiagramWidthInPixels(svg: String?) =
            if (svg != null)
                "viewBox=\"\\d+ \\d+ (\\d+) \\d+\"".toRegex()
                    .find(svg)
                    ?.let { it.groupValues[1].toInt() }
                    ?: throw IllegalStateException("No viewBox attribute found in SVG!")
            else null
    }
}
