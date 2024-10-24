package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.HTML
import nl.avisi.structurizr.site.generatr.site.model.SoftwareSystemDynamicPageViewModel

fun HTML.softwareSystemDynamicPage(viewModel: SoftwareSystemDynamicPageViewModel) {
    if (viewModel.visible)
        softwareSystemPage(viewModel) {
            diagramIndexList(viewModel.diagramIndexListViewModel)
            viewModel.diagrams.forEach {
                diagram(it)
            }
        }
    else
        redirectUpPage()
}
