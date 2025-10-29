package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.*
import nl.avisi.structurizr.site.generatr.site.model.BaseSoftwareSystemContainerSectionsPageViewModel
import nl.avisi.structurizr.site.generatr.site.model.SoftwareSystemContainerSectionsPageViewModel

fun HTML.softwareSystemContainerSectionsPage(viewModel: SoftwareSystemContainerSectionsPageViewModel) {
    if (viewModel.onlyComponentsDocumentationSectionsVisible)
        redirectRelative(
            viewModel.componentSectionsTabs.first().link.relativeHref
        )
    else if (viewModel.visible)
        softwareSystemPage(viewModel) {
            softwareSystemContainerSectionsBody(viewModel)
            table(viewModel.sectionsTable)
        }
    else
        redirectUpPage()
}

fun FlowContent.softwareSystemContainerSectionsBody(viewModel: BaseSoftwareSystemContainerSectionsPageViewModel) {
    softwareSystemSectionsBody(viewModel)

    div(classes = "tabs is-size-7") {
        ul(classes = "m-0 is-flex-wrap-wrap is-flex-shrink-1 is-flex-grow-0") {
            viewModel.componentSectionsTabs.forEach { tab ->
                li(classes = if (tab.link.active) "is-active" else null) {
                    link(tab.link)
                }
            }
        }
    }
}
