package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.FlowContent
import kotlinx.html.HTML
import kotlinx.html.div
import kotlinx.html.li
import kotlinx.html.ul
import nl.avisi.structurizr.site.generatr.site.model.BaseSoftwareSystemContainerDecisionsPageViewModel
import nl.avisi.structurizr.site.generatr.site.model.SoftwareSystemContainerDecisionsPageViewModel

fun HTML.softwareSystemContainerDecisionsPage(viewModel: SoftwareSystemContainerDecisionsPageViewModel) {
    if (viewModel.onlyComponentDecisionsVisible) {
        redirectRelative(
            viewModel.componentDecisionsTabs.first().link.relativeHref
        )
    } else if (viewModel.visible) {
        softwareSystemPage(viewModel) {
            decisionTabs(viewModel)
            componentDecisionTabs(viewModel)
            table(viewModel.decisionsTable)
        }
    } else {
        redirectUpPage()
    }
}

fun FlowContent.componentDecisionTabs(viewModel: BaseSoftwareSystemContainerDecisionsPageViewModel) {
    div(classes = "tabs is-size-7") {
        ul(classes = "m-0 is-flex-wrap-wrap is-flex-shrink-1 is-flex-grow-0") {
            viewModel.componentDecisionsTabs.forEach { tab ->
                li(classes = if (tab.link.active) "is-active" else null) {
                    link(tab.link)
                }
            }
        }
    }
}
