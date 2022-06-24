package nl.avisi.structurizr.site.generatr.site.pages.softwaresystem

import com.structurizr.model.Location
import com.structurizr.model.Relationship
import com.structurizr.model.SoftwareSystem
import kotlinx.html.*
import nl.avisi.structurizr.site.generatr.site.components.contentDiv
import nl.avisi.structurizr.site.generatr.site.context.AbstractSoftwareSystemPageContext
import nl.avisi.structurizr.site.generatr.site.context.SoftwareSystemContextPageContext
import nl.avisi.structurizr.site.generatr.site.context.SoftwareSystemDependenciesPageContext

fun DIV.dependenciesFragment(context: SoftwareSystemDependenciesPageContext) {
    contentDiv {
        h1 { +"Dependencies" }

        val dependencies = context.workspace.model.relationships.filter {
            it.source is SoftwareSystem && it.destination == context.softwareSystem ||
                    it.destination is SoftwareSystem && it.source == context.softwareSystem
        }
        if (dependencies.isNotEmpty()) {
            table(classes = "table") {
                thead {
                    tr {
                        th { +"Source" }
                        th { +"Description" }
                        th { +"Destination" }
                        th { +"Technology" }
                    }
                }
                tbody {
                    dependencies
                        .sortedBy { it.source.name + it.destination.name }
                        .forEach { dependencyTableRow(context, it) }
                }
            }
        }
    }
}

private fun TBODY.dependencyTableRow(context: AbstractSoftwareSystemPageContext, relationship: Relationship) {
    val source = relationship.source as SoftwareSystem
    val destination = relationship.destination as SoftwareSystem

    tr {
        th { softwareSystemLink(context, source) }
        td { +relationship.description }
        th { softwareSystemLink(context, destination) }
        td { +relationship.technology }
    }
}

private fun TH.softwareSystemLink(context: AbstractSoftwareSystemPageContext, destination: SoftwareSystem) {
    if (destination == context.softwareSystem)
        +destination.name
    else if (destination.location == Location.Internal)
        a(href = SoftwareSystemContextPageContext(context.generatorContext, destination).urlRelativeTo(context)) {
            +destination.name
        }
    else
        span(classes = "has-text-grey") { +"${destination.name} (External)" }
}
