package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.*
import nl.avisi.structurizr.site.generatr.site.model.SoftwareSystemContainerDecisionsPageViewModel

fun HTML.softwareSystemContainerDecisionsPage(viewModel: SoftwareSystemContainerDecisionsPageViewModel) {
    if (viewModel.visible)
        softwareSystemPage(viewModel) {
            div(classes = "tabs") {
                ul(classes = "m-0") {
                    viewModel.decisionTabs
                            .filter { it.visible }
                            .forEach {
                                li(classes = if (it.link.active) "is-active" else null) {
                                    link(it.link)
                                }
                            }
                }
            }
            table(viewModel.decisionsTable)
        }
    else
        redirectUpPage()
}
