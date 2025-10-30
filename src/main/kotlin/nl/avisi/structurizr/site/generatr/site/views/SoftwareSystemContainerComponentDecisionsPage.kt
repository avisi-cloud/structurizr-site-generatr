package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.HTML
import nl.avisi.structurizr.site.generatr.site.model.SoftwareSystemContainerComponentDecisionsPageViewModel

fun HTML.softwareSystemContainerComponentDecisionsPage(
    viewModel: SoftwareSystemContainerComponentDecisionsPageViewModel
) {
    if (viewModel.visible) {
        softwareSystemPage(viewModel) {
            decisionTabs(viewModel)
            componentDecisionTabs(viewModel)
            table(viewModel.decisionsTable)
        }
    } else {
        redirectUpPage()
    }
}
