package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.*
import nl.avisi.structurizr.site.generatr.site.model.BaseSoftwareSystemContainerSectionsPageViewModel
import nl.avisi.structurizr.site.generatr.site.model.SoftwareSystemContainerSectionsPageViewModel

fun HTML.softwareSystemContainerSectionsPage(viewModel: SoftwareSystemContainerSectionsPageViewModel) {
    if (viewModel.visible)
        softwareSystemPage(viewModel) {
            softwareSystemContainerSectionsBody(viewModel)
        }
    else
        redirectUpPage()
}

fun FlowContent.softwareSystemContainerSectionsBody(viewModel: BaseSoftwareSystemContainerSectionsPageViewModel) {
    softwareSystemSectionsBody(viewModel)
    table(viewModel.sectionsTable)

    div(classes = "tabs") {
        ul(classes = "m-0 is-flex-wrap-wrap is-flex-shrink-1 is-flex-grow-0") {
            viewModel.componentSectionsTabs.forEach { tab ->
                li(classes = if (tab.link.active) "is-active" else null) {
                    link(tab.link)
                }
            }
        }
    }
}
