package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.HTML
import kotlinx.html.div
import kotlinx.html.li
import kotlinx.html.ul
import nl.avisi.structurizr.site.generatr.site.model.SoftwareSystemContainerDecisionsPageViewModel

fun HTML.softwareSystemContainerDecisionsPage(viewModel: SoftwareSystemContainerDecisionsPageViewModel) {
    if (viewModel.visible)
        softwareSystemPage(viewModel) {
            div(classes = "tabs") {
                ul(classes = "m-0") {
                    viewModel.decisionTabs
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
