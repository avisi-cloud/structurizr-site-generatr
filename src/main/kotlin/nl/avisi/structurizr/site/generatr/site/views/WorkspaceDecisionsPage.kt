package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.*
import nl.avisi.structurizr.site.generatr.site.components.contentDiv
import nl.avisi.structurizr.site.generatr.site.model.TableViewModel
import nl.avisi.structurizr.site.generatr.site.model.WorkspaceDecisionsPageViewModel

fun HTML.workspaceDecisionsPage(viewModel: WorkspaceDecisionsPageViewModel) {
    page(viewModel) {
        contentDiv {
            h2 { +viewModel.pageSubTitle }
            table {
                thead {
                    viewModel.decisionsTable.headerRows.forEach {
                        row(it)
                    }
                }
                tbody {
                    viewModel.decisionsTable.bodyRows.forEach {
                        row(it)
                    }
                }
            }
        }
    }
}

private fun THEAD.row(viewModel: TableViewModel.RowViewModel) {
    tr {
        viewModel.columns.forEach {
            cell(it)
        }
    }
}

private fun TBODY.row(viewModel: TableViewModel.RowViewModel) {
    tr {
        viewModel.columns.forEach {
            cell(it)
        }
    }
}

private fun TR.cell(viewModel: TableViewModel.CellViewModel) {
    when (viewModel) {
        is TableViewModel.TextCellViewModel -> td { +viewModel.title }
        is TableViewModel.LinkCellViewModel -> td { a(href = viewModel.link.relativeHref) { +viewModel.link.title } }
    }
}
