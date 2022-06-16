package nl.avisi.structurizr.site.generatr.site.pages

import com.structurizr.model.Location
import kotlinx.html.*
import nl.avisi.structurizr.site.generatr.site.components.contentDiv
import nl.avisi.structurizr.site.generatr.site.components.page
import nl.avisi.structurizr.site.generatr.site.context.SoftwareSystemContextPageContext
import nl.avisi.structurizr.site.generatr.site.context.SoftwareSystemsOverviewPageContext

fun HTML.softwareSystemsOverviewPage(context: SoftwareSystemsOverviewPageContext) {
    page(context = context) {
        contentDiv {
            h2 { +context.title }
            table(classes = "table") {
                thead {
                    tr {
                        th { +"Name" }
                        th { +"Description" }
                    }
                }
                tbody {
                    context.workspace.model.softwareSystems
                        .sortedBy { it.name.lowercase() }
                        .forEach {
                            tr {
                                th {
                                    if (it.location == Location.Internal)
                                        a(href = SoftwareSystemContextPageContext(context.generatorContext, it).url) {
                                            +it.name
                                        }
                                    else
                                        span(classes = "has-text-grey") { +"${it.name} (External)" }
                                }
                                td { +it.description }
                            }
                        }
                }
            }
        }
    }
}
