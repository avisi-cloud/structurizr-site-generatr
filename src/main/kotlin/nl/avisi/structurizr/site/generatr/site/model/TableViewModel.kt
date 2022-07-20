package nl.avisi.structurizr.site.generatr.site.model

data class TableViewModel(val headerRows: List<RowViewModel>, val bodyRows: List<RowViewModel>) {
    sealed interface CellViewModel {
        val isHeader: Boolean
    }

    data class TextCellViewModel(val title: String, override val isHeader: Boolean, val greyText: Boolean = false) :
        CellViewModel {
        override fun toString() = if (isHeader)
            "headerCell($title, greyText=$greyText)"
        else
            "cell($title, greyText=$greyText)"
    }

    data class LinkCellViewModel(val link: LinkViewModel, override val isHeader: Boolean) : CellViewModel {
        override fun toString() = if (isHeader) "headerCell($link)" else "cell($link)"
    }

    data class RowViewModel(val columns: List<CellViewModel>) {
        override fun toString() =
            columns.joinToString(separator = ", ", prefix = "row { ", postfix = " }") { it.toString() }
    }

    class TableViewInitializerContext(
        private val headerRows: MutableList<RowViewModel>,
        private val bodyRows: MutableList<RowViewModel>
    ) {
        fun headerRow(vararg cells: TextCellViewModel) {
            headerRows.add(RowViewModel(cells.toList()))
        }

        fun bodyRow(vararg cells: CellViewModel) {
            bodyRows.add(RowViewModel(cells.toList()))
        }

        fun headerCell(title: String, greyText: Boolean = false) = TextCellViewModel(title, true, greyText)
        fun headerCellWithLink(pageViewModel: PageViewModel, title: String, href: String) =
            LinkCellViewModel(LinkViewModel(pageViewModel, title, href), true)

        fun cell(title: String): TextCellViewModel = TextCellViewModel(title, false)
        fun cellWithLink(pageViewModel: PageViewModel, title: String, href: String) =
            LinkCellViewModel(LinkViewModel(pageViewModel, title, href), false)
    }

    override fun toString(): String {
        val builder = StringBuilder()
        builder.appendLine("header {")
        headerRows.forEach {
            builder.append("  ")
            builder.append(it)
            builder.appendLine()
        }
        builder.appendLine("}")
        builder.appendLine("body {")
        bodyRows.forEach {
            builder.append("  ")
            builder.append(it)
            builder.appendLine()
        }
        builder.appendLine("}")
        return builder.toString()
    }

    companion object {
        fun create(initializer: TableViewInitializerContext.() -> Unit): TableViewModel {
            val headerRows = mutableListOf<RowViewModel>()
            val bodyRows = mutableListOf<RowViewModel>()
            val ctx = TableViewInitializerContext(headerRows, bodyRows)
            ctx.initializer()

            return TableViewModel(headerRows, bodyRows)
        }
    }
}
