package nl.avisi.structurizr.site.generatr.site.views

import kotlinx.html.*
import nl.avisi.structurizr.site.generatr.site.model.TableViewModel
import nl.avisi.structurizr.site.generatr.site.model.CellWidth

fun FlowContent.table(viewModel: TableViewModel) {
    table (classes = "table is-fullwidth") {
        thead {
            viewModel.headerRows.forEach {
                row(it)
            }
        }
        tbody {
            viewModel.bodyRows.forEach {
                row(it)
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
        is TableViewModel.TextCellViewModel -> {
            val classes = when (viewModel.width) {
                CellWidth.UNSPECIFIED -> null
                CellWidth.ONE_TENTH -> "is-one-tenth"
                CellWidth.ONE_FOURTH -> "is-one-fourth"
                CellWidth.TWO_FOURTH -> "is-two-fourth"
            }

            var spanClasses = ""
            if (viewModel.greyText) spanClasses += "has-text-grey "
            if (viewModel.boldText) spanClasses += "has-text-weight-bold"

            if (viewModel.isHeader)
                th(classes = classes) { span(classes = spanClasses) { +viewModel.title } }
            else
                td(classes = classes) { span(classes = spanClasses) { +viewModel.title } }
        }
        is TableViewModel.LinkCellViewModel -> {
            val classes = if (viewModel.boldText) "has-text-weight-bold" else null
            if (viewModel.isHeader)
                th(classes = classes)  { link(viewModel.link) }
            else
                td(classes = classes)  { link(viewModel.link) }
        }
        is TableViewModel.ExternalLinkCellViewModel ->
            if (viewModel.isHeader)
                th { externalLink(viewModel.link) }
            else
                td { externalLink(viewModel.link) }
    }
}
