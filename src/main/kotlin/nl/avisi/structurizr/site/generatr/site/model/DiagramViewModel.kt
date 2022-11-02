package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.view.View
import nl.avisi.structurizr.site.generatr.site.GeneratorContext
import java.io.File

data class DiagramViewModel(
    val name: String,
    val svg: String,
    val svgLocation: ImageViewModel,
    val pngLocation: ImageViewModel,
    val pumlLocation: ImageViewModel
) {
    companion object {
        fun forView(pageViewModel: PageViewModel, view: View) = DiagramViewModel(
            view.name,
            File(svgPathname(pageViewModel.generatorContext, view))
                .let {
                    if (it.exists()) it.readText() else "<svg></svg>"
                },
            ImageViewModel(pageViewModel, "/svg/${view.key}.svg"),
            ImageViewModel(pageViewModel, "/png/${view.key}.png"),
            ImageViewModel(pageViewModel, "/puml/${view.key}.puml")
        )

        private fun svgPathname(generatorContext: GeneratorContext, view: View) =
            "${generatorContext.exportDir.absolutePath}/${generatorContext.currentBranch}/svg/${view.key}.svg"
    }
}
