package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.HTML
import kotlinx.html.h3
import nl.avisi.structurizr.site.generatr.site.model.SoftwareSystemDependenciesPageViewModel

fun HTML.softwareSystemDependenciesPage(viewModel: SoftwareSystemDependenciesPageViewModel) {
    softwareSystemPage(viewModel) {
        h3 { +"Inbound" }
        table(viewModel.dependenciesInboundTable)
        h3 { +"Outbound" }
        table(viewModel.dependenciesOutboundTable)
    }
}
