package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.HTML
import nl.avisi.structurizr.site.generatr.site.model.WorkspaceDocumentationSectionPageViewModel

fun HTML.workspaceDocumentationSectionPage(viewModel: WorkspaceDocumentationSectionPageViewModel) {
    page(viewModel) {
        contentDiv {
            markdown(viewModel, viewModel.content)
        }
    }
}
