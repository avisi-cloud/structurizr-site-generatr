package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.HTML
import kotlinx.html.h2
import nl.avisi.structurizr.site.generatr.site.model.SoftwareSystemDependenciesPageViewModel

fun HTML.softwareSystemDependenciesPage(viewModel: SoftwareSystemDependenciesPageViewModel) {
    softwareSystemPage(viewModel) {
        h2 { +"Inbound" }
        table(viewModel.dependenciesInboundTable)
        h2 { +"Outbound" }
        table(viewModel.dependenciesOutboundTable)
    }
}
