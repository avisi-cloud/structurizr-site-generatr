package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.view.View

data class DiagramViewModel(
    val name: String,
    val svg: String,
    val svgLocation: ImageViewModel,
    val pngLocation: ImageViewModel,
    val pumlLocation: ImageViewModel
) {
    companion object {
        fun forView(pageViewModel: PageViewModel, view: View, svgFactory: (name: String) -> String) = DiagramViewModel(
            view.name,
            svgFactory(view.key),
            ImageViewModel(pageViewModel, "/svg/${view.key}.svg"),
            ImageViewModel(pageViewModel, "/png/${view.key}.png"),
            ImageViewModel(pageViewModel, "/puml/${view.key}.puml")
        )
    }
}
