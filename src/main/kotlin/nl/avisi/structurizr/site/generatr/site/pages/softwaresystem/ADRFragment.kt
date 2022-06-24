package nl.avisi.structurizr.site.generatr.site.pages.softwaresystem

import kotlinx.html.*
import nl.avisi.structurizr.site.generatr.site.components.contentDiv
import nl.avisi.structurizr.site.generatr.site.context.AbstractSoftwareSystemPageContext
import nl.avisi.structurizr.site.generatr.site.context.SoftwareSystemDecisionPageContext
import java.time.ZoneId
import java.util.*

fun DIV.adrFragment(context: AbstractSoftwareSystemPageContext) {
    contentDiv {
        h1 { +"Architecture decision records" }
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
                context.adrs.sortedBy { it.id.toInt() }.forEach {
                    tr {
                        th { +it.id }
                        td { +formatDate(it.date) }
                        td { +it.status.toString() }
                        td {
                            a(
                                href = SoftwareSystemDecisionPageContext(
                                    context.generatorContext,
                                    context.softwareSystem,
                                    it
                                ).urlRelativeTo(context)
                            ) {
                                +it.title
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun formatDate(date: Date): String {
    val localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    return String.format("%02d-%02d-%04d", localDate.dayOfMonth, localDate.monthValue, localDate.year)
}
