package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.HTML
import nl.avisi.structurizr.site.generatr.site.model.WorkspaceDecisionPageViewModel

fun HTML.workspaceDecisionPage(viewModel: WorkspaceDecisionPageViewModel) {
    page(viewModel) {
        contentDiv {
            rawHtml(viewModel.content)
        }
    }
}
