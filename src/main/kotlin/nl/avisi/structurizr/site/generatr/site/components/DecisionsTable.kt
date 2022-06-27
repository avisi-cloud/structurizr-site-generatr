package nl.avisi.structurizr.site.generatr.site.components

import com.structurizr.documentation.Decision
import kotlinx.html.*
import nl.avisi.structurizr.site.generatr.site.context.AbstractPageContext
import nl.avisi.structurizr.site.generatr.site.formatDate

fun DIV.decisionsTable(
    context: AbstractPageContext,
    decisions: Collection<Decision>,
    createContext: (Decision) -> AbstractPageContext
) {
    table(classes = "table") {
        thead {
            tr {
                th { +"ID" }
                th { +"Datum" }
                th { +"Status" }
                th { +"Title" }
            }
        }
        tbody {
            decisions.sortedBy { it.id.toInt() }.forEach {
                tr {
                    th { +it.id }
                    td { +formatDate(it.date) }
                    td { +it.status.toString() }
                    td {
                        a(href = createContext(it).urlRelativeTo(context)) {
                            +it.title
                        }
                    }
                }
            }
        }
    }
}
