package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.HTML
import nl.avisi.structurizr.site.generatr.site.model.SoftwareSystemContainerPageViewModel

fun HTML.softwareSystemContainerPage(viewModel: SoftwareSystemContainerPageViewModel) {
    if (viewModel.visible)
        softwareSystemPage(viewModel) {
            diagramIndexList(viewModel.diagramIndexListViewModel)
            viewModel.diagrams.forEach { diagram(it) }
            viewModel.images.forEach { image(it) }
        }
    else
        redirectUpPage()
}
