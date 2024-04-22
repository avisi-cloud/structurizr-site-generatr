package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.HTML
import nl.avisi.structurizr.site.generatr.normalize
import nl.avisi.structurizr.site.generatr.site.model.SoftwareSystemComponentsPageViewModel

fun HTML.softwareSystemComponentsPage(viewModel: SoftwareSystemComponentsPageViewModel) {
    viewModel.redirectLink?.let { redirectRelative(it.normalize()) }
}
