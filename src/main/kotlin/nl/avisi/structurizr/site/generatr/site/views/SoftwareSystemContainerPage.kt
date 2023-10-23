package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.HTML
import nl.avisi.structurizr.site.generatr.site.model.SoftwareSystemContainerPageViewModel

fun HTML.softwareSystemContainerPage(viewModel: SoftwareSystemContainerPageViewModel) {
    if (viewModel.diagramsVisible || viewModel.imagesVisible)
        softwareSystemPage(viewModel) {
            if (viewModel.diagramsVisible) {
                viewModel.diagrams.forEach {
                    diagram(it)
                }
            }
            if (viewModel.imagesVisible) {
                viewModel.images.forEach {
                    rawImage(it.content)
                }
            }
        }
    else
        redirectUpPage()
}
