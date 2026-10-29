package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.view.View

data class DiagramViewModel(
    val key: String,
    val title: String,
    val description: String?,
    val svg: String?,
    val diagramWidthInPixels: Int?,
    val svgLocation: ImageViewModel,
    val pngLocation: ImageViewModel,
    val pumlLocation: ImageViewModel,
    val legendSvg: String? = null,
    val legendWidthInPixels: Int? = null,
    val legendSvgLocation: ImageViewModel? = null,
    val legendPngLocation: ImageViewModel? = null,
    val legendPumlLocation: ImageViewModel? = null
) {
    val hasLegend: Boolean get() = legendSvg != null

    companion object {
        fun forView(
            pageViewModel: PageViewModel,
            view: View,
            svgFactory: (key: String, url: String) -> String?,
            legendSvgFactory: (key: String) -> String? = { null }
        ) = forView(
            pageViewModel, view.key, view.name, view.title, view.description.ifBlank { null },
            svgFactory, legendSvgFactory
        )

        fun forView(
            pageViewModel: PageViewModel,
            key: String,
            name: String,
            title: String?,
            description: String?,
            svgFactory: (key: String, url: String) -> String?,
            legendSvgFactory: (key: String) -> String? = { null }
        ): DiagramViewModel {
            val svg = svgFactory(key, pageViewModel.url)
            val legendSvg = legendSvgFactory(key)
            return DiagramViewModel(
                key,
                title ?: name,
                description,
                svg,
                extractDiagramWidthInPixels(svg),
                ImageViewModel(pageViewModel, "/svg/$key.svg"),
                ImageViewModel(pageViewModel, "/png/$key.png"),
                ImageViewModel(pageViewModel, "/puml/$key.puml"),
                legendSvg,
                extractDiagramWidthInPixels(legendSvg, required = false),
                if (legendSvg != null) ImageViewModel(pageViewModel, "/svg/$key.legend.svg") else null,
                if (legendSvg != null) ImageViewModel(pageViewModel, "/png/$key.legend.png") else null,
                if (legendSvg != null) ImageViewModel(pageViewModel, "/puml/$key.legend.puml") else null
            )
        }

        private fun extractDiagramWidthInPixels(svg: String?, required: Boolean = true) =
            if (svg != null)
                "viewBox=\"\\d+ \\d+ (\\d+) \\d+\"".toRegex()
                    .find(svg)
                    ?.let { it.groupValues[1].toInt() }
                    ?: if (required) throw IllegalStateException("No viewBox attribute found in SVG!") else null
            else null
    }
}
