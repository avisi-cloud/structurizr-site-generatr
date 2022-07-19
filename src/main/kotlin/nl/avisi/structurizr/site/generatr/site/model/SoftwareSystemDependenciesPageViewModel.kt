package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.model.Relationship
import com.structurizr.model.SoftwareSystem
import nl.avisi.structurizr.site.generatr.site.GeneratorContext

class SoftwareSystemDependenciesPageViewModel(
    generatorContext: GeneratorContext,
    private val softwareSystem: SoftwareSystem
) : SoftwareSystemPageViewModel(generatorContext, softwareSystem, Tab.DEPENDENCIES) {
    val dependenciesTable = TableViewModel.create {
        header()

        incomingAndOutgoingRelationships.forEach {
            bodyRow(it)
        }
    }

    private val incomingAndOutgoingRelationships
        get() = generatorContext.workspace.model.relationships.asSequence()
            .filter { it.destination == softwareSystem }
            .plus(softwareSystem.relationships)
            .filter { it.source is SoftwareSystem && it.destination is SoftwareSystem }
            .sortedBy { it.source.name }

    private fun TableViewModel.TableViewInitializerContext.header() {
        headerRow(
            headerCell("Source"),
            headerCell("Description"),
            headerCell("Destination"),
            headerCell("Technology")
        )
    }

    private fun TableViewModel.TableViewInitializerContext.bodyRow(relationship: Relationship) {
        bodyRow(
            softwareSystemCell(relationship.source as SoftwareSystem),
            cell(relationship.description),
            softwareSystemCell(relationship.destination as SoftwareSystem),
            cell(relationship.technology)
        )
    }

    private fun TableViewModel.TableViewInitializerContext.softwareSystemCell(system: SoftwareSystem) =
        if (system == softwareSystem)
            headerCell(system.name)
        else
            headerCellWithLink(
                this@SoftwareSystemDependenciesPageViewModel, system.name,
                url(system, Tab.HOME)
            )
}
