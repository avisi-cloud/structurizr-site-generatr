package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.view.View

data class LegendViewModel(
    val svg: String?,
    val widthInPixels: Int?,
    val svgLocation: ImageViewModel,
    val pngLocation: ImageViewModel,
    val pumlLocation: ImageViewModel,
)

data class DiagramViewModel(
    val key: String,
    val title: String,
    val description: String?,
    val svg: String?,
    val diagramWidthInPixels: Int?,
    val svgLocation: ImageViewModel,
    val pngLocation: ImageViewModel,
    val pumlLocation: ImageViewModel,
    val legend: LegendViewModel,
) {
    companion object {
        fun forView(pageViewModel: PageViewModel, view: View, svgFactory: (key: String, url: String) -> Pair<String, String>?) =
            forView(pageViewModel, view.key, view.name, view.title, view.description.ifBlank { null }, svgFactory)

        fun forView(
            pageViewModel: PageViewModel,
            key: String,
            name: String,
            title: String?,
            description: String?,
            svgFactory: (key: String, url: String) -> Pair<String, String>?
        ): DiagramViewModel {
            val (svg, legendSvg) = svgFactory(key, pageViewModel.url) ?: (null to null)
            return DiagramViewModel(
                key,
                title ?: name,
                description,
                svg,
                extractDiagramWidthInPixels(svg),
                ImageViewModel(pageViewModel, "/svg/$key.svg"),
                ImageViewModel(pageViewModel, "/png/$key.png"),
                ImageViewModel(pageViewModel, "/puml/$key.puml"),
                LegendViewModel(
                    legendSvg,
                    extractDiagramWidthInPixels(legendSvg),
                    ImageViewModel(pageViewModel, "/svg/$key.legend.svg"),
                    ImageViewModel(pageViewModel, "/png/$key.legend.png"),
                    ImageViewModel(pageViewModel, "/puml/$key.legend.puml"),
                )
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
