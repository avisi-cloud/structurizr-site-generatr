package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.HTML
import nl.avisi.structurizr.site.generatr.site.model.SoftwareSystemDependenciesPageViewModel

fun HTML.softwareSystemDependenciesPage(viewModel: SoftwareSystemDependenciesPageViewModel) {
    softwareSystemPage(viewModel) {
        table(viewModel.dependenciesTable)
    }
}
