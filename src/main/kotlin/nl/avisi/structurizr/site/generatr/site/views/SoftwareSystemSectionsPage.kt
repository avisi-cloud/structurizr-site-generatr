package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.*
import nl.avisi.structurizr.site.generatr.site.model.BaseSoftwareSystemSectionsPageViewModel
import nl.avisi.structurizr.site.generatr.site.model.SoftwareSystemSectionsPageViewModel

fun HTML.softwareSystemSectionsPage(viewModel: SoftwareSystemSectionsPageViewModel) {
    if (viewModel.visible)
        softwareSystemPage(viewModel) {
            softwareSystemSectionsBody(viewModel)
            table(viewModel.sectionsTable)
        }
    else
        redirectUpPage()
}

fun FlowContent.softwareSystemSectionsBody(viewModel: BaseSoftwareSystemSectionsPageViewModel) {
    div(classes = "tabs") {
        ul(classes = "m-0 is-flex-wrap-wrap is-flex-shrink-1 is-flex-grow-0") {
            viewModel.sectionsTabs
                .forEach {
                    li(classes = if (it.link.active) "is-active" else null) {
                        link(it.link)
                    }
                }
        }
    }
}
