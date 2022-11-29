package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.HTML
import nl.avisi.structurizr.site.generatr.site.model.SoftwareSystemSectionsPageViewModel

fun HTML.softwareSystemSectionsPage(viewModel: SoftwareSystemSectionsPageViewModel) {
    if (viewModel.visible)
        softwareSystemPage(viewModel) {
            table(viewModel.sectionsTable)
        }
    else
        redirectUpPage()
}
