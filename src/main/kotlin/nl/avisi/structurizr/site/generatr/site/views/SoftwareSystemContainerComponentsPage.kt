package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.*
import nl.avisi.structurizr.site.generatr.site.model.SoftwareSystemContainerComponentsPageViewModel

fun HTML.softwareSystemContainerComponentsPage(viewModel: SoftwareSystemContainerComponentsPageViewModel) {
     if (viewModel.visible) {
         softwareSystemPage(viewModel) {
             nav(classes = "nhsuk-contents-list") {
                 role = "navigation"
                 attributes["aria-label"] = "Page navigation"
                 h2("nhsuk-u-visually-hidden") {
                     +"Contents"
                 }
                 ol("nhsuk-contents-list__list") {
                     viewModel.containerTabs
                             .forEach {
                                 li("nhsuk-contents-list__list_item") {
                                     link(it.link, if (it.link.active) "nhsuk-contents-list__current" else "nhsuk-contents-list__item")
                                 }
                             }
                 }
             }
             diagramIndex(viewModel.diagramIndex)
             viewModel.diagrams.forEach { diagram(it) }
             viewModel.images.forEach { image(it) }

             if (viewModel.hasProperties) {
                 h3 { +"Properties" }
                 table(viewModel.propertiesTable)
             }
         }
     }
    else
        redirectUpPage()
    }




