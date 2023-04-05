package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.model.Relationship
import com.structurizr.model.SoftwareSystem
import nl.avisi.structurizr.site.generatr.site.GeneratorContext

class SoftwareSystemDependenciesPageViewModel(generatorContext: GeneratorContext,private val softwareSystem: SoftwareSystem
) : SoftwareSystemPageViewModel(generatorContext, softwareSystem, Tab.DEPENDENCIES) {

    val dependenciesInboundTable = TableViewModel.create {
        header()
        incomingRelationships.forEach {inboundBodyRow(it)}
    }

    val dependenciesOutboundTable = TableViewModel.create {
        header()
        outgoingRelationships.forEach {outboundBodyRow(it)}
    }

    private val incomingRelationships
        get() = generatorContext.workspace.model.relationships.asSequence()
            .filter { it.destination == softwareSystem }
            .plus(softwareSystem.relationships)
            .filter { it.source is SoftwareSystem && it.destination is SoftwareSystem}
            .filter { it.destination == softwareSystem}
            .sortedBy { it.source.name.lowercase() }

    private val outgoingRelationships
        get() = generatorContext.workspace.model.relationships.asSequence()
            .filter { it.destination == softwareSystem }
            .plus(softwareSystem.relationships)
            .filter { it.source is SoftwareSystem && it.destination is SoftwareSystem }
            .filter { it.source == softwareSystem}
            .sortedBy { it.destination.name.lowercase() }

    private fun TableViewModel.TableViewInitializerContext.header() {
        headerRow(
            headerCell("System"),
            headerCell("Description"),
            headerCell("Technology")
        )
    }

    private fun TableViewModel.TableViewInitializerContext.inboundBodyRow(relationship: Relationship) {
        bodyRow(
            softwareSystemDependencyCell(relationship.source as SoftwareSystem),
            cell(relationship.description),
            cell(relationship.technology)
        )
    }

    private fun TableViewModel.TableViewInitializerContext.outboundBodyRow(relationship: Relationship) {
        bodyRow(
            softwareSystemDependencyCell(relationship.destination as SoftwareSystem),
            cell(relationship.description),
            cell(relationship.technology)
        )
    }

    private fun TableViewModel.TableViewInitializerContext.softwareSystemDependencyCell(system: SoftwareSystem) =
        if (system == softwareSystem)
            headerCell(system.name)
        else
            softwareSystemCell(this@SoftwareSystemDependenciesPageViewModel, system)
}
