package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.HTML
import kotlinx.html.div
import kotlinx.html.li
import kotlinx.html.ul
import nl.avisi.structurizr.site.generatr.site.model.SoftwareSystemContainerComponentSectionsPageViewModel

fun HTML.softwareSystemContainerComponentSectionsPage(viewModel: SoftwareSystemContainerComponentSectionsPageViewModel) {
    if (viewModel.visible)
        softwareSystemPage(viewModel) {
            softwareSystemContainerSectionBody(viewModel)
            table(viewModel.componentSectionTable)
        }
    else
        redirectUpPage()
}
