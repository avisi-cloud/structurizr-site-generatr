package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.*
import nl.avisi.structurizr.site.generatr.site.model.SoftwareSystemContainerComponentsPageViewModel

fun HTML.softwareSystemContainerComponentsPage(viewModel: SoftwareSystemContainerComponentsPageViewModel) {
     if (viewModel.visible) {
        softwareSystemPage(viewModel) {
            div(classes = "tabs") {
                ul(classes = "m-0 is-flex-wrap-wrap is-flex-shrink-1 is-flex-grow-0") {
                    viewModel.containerTabs
                        .forEach {
                            li(classes = if (it.link.active) "is-active" else null) {
                                link(it.link)
                            }
                        }
                }
            }
            diagramIndexList(viewModel.diagramIndex)
            viewModel.diagrams.forEach { diagram(it) }
            viewModel.images.forEach { image(it) }

            if(viewModel.hasProperties) {
                h3 { +"Properties" }
                table(viewModel.propertiesTable)
            }
        }
    } else
        redirectUpPage()
}
