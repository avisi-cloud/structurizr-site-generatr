package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.view.View

data class DiagramViewModel(
    val name: String,
    val svgLocation: ImageViewModel,
    val pngLocation: ImageViewModel,
    val pumlLocation: ImageViewModel
) {
    companion object {
        fun forView(pageViewModel: PageViewModel, view: View) = DiagramViewModel(
            view.name,
            ImageViewModel(pageViewModel, "/svg/${view.key}.svg"),
            ImageViewModel(pageViewModel, "/png/${view.key}.png"),
            ImageViewModel(pageViewModel, "/puml/${view.key}.puml")
        )
    }
}
