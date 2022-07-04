package nl.avisi.structurizr.site.generatr.site.model

data class TableViewModel(val headerRows: List<RowViewModel>, val bodyRows: List<RowViewModel>) {
    interface CellViewModel {
        val title: String
        val isHeader: Boolean
    }

    data class TextCellViewModel(override val title: String, override val isHeader: Boolean) : CellViewModel
    data class LinkCellViewModel(
        private val pageViewModel: PageViewModel,
        override val title: String,
        val href: String,
        override val isHeader: Boolean
    ) : CellViewModel {
        val link = LinkViewModel(pageViewModel, title, href)
    }

    data class RowViewModel(val columns: List<CellViewModel>)

    class TableViewInitializerContext(private val pageViewModel: PageViewModel) {
        val headerRows = mutableListOf<RowViewModel>()
        val bodyRows = mutableListOf<RowViewModel>()

        fun headerRow(vararg cells: TextCellViewModel) {
            headerRows.add(RowViewModel(cells.toList()))
        }

        fun bodyRow(vararg cells: CellViewModel) {
            bodyRows.add(RowViewModel(cells.toList()))
        }

        fun headerCell(title: String) = TextCellViewModel(title, true)

        fun cell(title: String): TextCellViewModel = TextCellViewModel(title, false)
        fun cellWithLink(title: String, href: String) = LinkCellViewModel(pageViewModel, title, href, false)
    }

    companion object {
        fun create(pageViewModel: PageViewModel, initializer: TableViewInitializerContext.() -> Unit): TableViewModel {
            val ctx = TableViewInitializerContext(pageViewModel)
            ctx.initializer()

            return TableViewModel(ctx.headerRows, ctx.bodyRows)
        }
    }
}
