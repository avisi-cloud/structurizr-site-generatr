package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.HTML
import nl.avisi.structurizr.site.generatr.site.model.SoftwareSystemContainerPageViewModel

fun HTML.softwareSystemContainerPage(viewModel: SoftwareSystemContainerPageViewModel) {
    softwareSystemPage(viewModel) {
        viewModel.diagrams.forEach {
            diagram(it)
        }
    }
}
