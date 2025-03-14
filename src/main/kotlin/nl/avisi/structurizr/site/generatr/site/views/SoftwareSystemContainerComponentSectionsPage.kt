package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.HTML
import nl.avisi.structurizr.site.generatr.site.model.SoftwareSystemContainerComponentSectionsPageViewModel

fun HTML.softwareSystemContainerComponentSectionsPage(viewModel: SoftwareSystemContainerComponentSectionsPageViewModel) {
    if (viewModel.visible)
        softwareSystemPage(viewModel) {
            softwareSystemContainerSectionsBody(viewModel)
            table(viewModel.componentSectionTable)
        }
    else
        redirectUpPage()
}
