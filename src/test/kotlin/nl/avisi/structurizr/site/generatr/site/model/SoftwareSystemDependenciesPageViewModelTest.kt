package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.*
import com.structurizr.model.Location
import kotlin.test.Test

class SoftwareSystemDependenciesPageViewModelTest : ViewModelTest() {
    private val generatorContext = generatorContext()
    private val softwareSystem1 = generatorContext.workspace.model.addSoftwareSystem("Software system 1")
    private val softwareSystem2 = generatorContext.workspace.model.addSoftwareSystem("Software system 2")

    @Test
    fun `active tab`() {
        val viewModel = SoftwareSystemDependenciesPageViewModel(generatorContext, softwareSystem1)

        assertThat(viewModel.tabs.single { it.link.active }.tab)
            .isEqualTo(SoftwareSystemPageViewModel.Tab.DEPENDENCIES)
    }

    @Test
    fun `empty table when no dependencies are present`() {
        val viewModel = SoftwareSystemDependenciesPageViewModel(generatorContext, softwareSystem1)

        assertThat(viewModel.dependenciesInboundTable).isEqualTo(
            TableViewModel.create {
                dependenciesTableHeader()
            }
        )
    }

    @Test
    fun `show outbound dependencies in table when present`() {
        softwareSystem1.uses(softwareSystem2, "Uses SOAP", "SOAP")
        val viewModel = SoftwareSystemDependenciesPageViewModel(generatorContext, softwareSystem1)

        assertThat(viewModel.dependenciesOutboundTable).isEqualTo(
            TableViewModel.create {
                dependenciesTableHeader()
                bodyRow(
                    headerCellWithLink(
                        viewModel, softwareSystem2.name,
                        SoftwareSystemPageViewModel.url(softwareSystem2, SoftwareSystemPageViewModel.Tab.HOME)
                    ),
                    cell("Uses SOAP"),
                    cell("SOAP"),
                )
            }
        )
    }

    @Test
    fun `show inbound dependencies in table when present`() {
        softwareSystem2.uses(softwareSystem1, "Uses REST", "REST")
        val viewModel = SoftwareSystemDependenciesPageViewModel(generatorContext, softwareSystem1)

        assertThat(viewModel.dependenciesInboundTable).isEqualTo(
            TableViewModel.create {
                dependenciesTableHeader()
                bodyRow(
                    headerCellWithLink(
                        viewModel, softwareSystem2.name,
                        SoftwareSystemPageViewModel.url(softwareSystem2, SoftwareSystemPageViewModel.Tab.HOME)
                    ),
                    cell("Uses REST"),
                    cell("REST"),
                )
            }
        )
    }

    @Test
    fun `only relationships from and to software systems`() {
        val backend1 = softwareSystem1.addContainer("Backend 1")
        val backend2 = softwareSystem2.addContainer("Backend 2")

        softwareSystem1.uses(softwareSystem2, "Uses REST", "REST")
        softwareSystem2.uses(softwareSystem1, "Uses SOAP", "SOAP")
        backend1.uses(softwareSystem2, "Uses from container 1 to system 2")
        backend2.uses(softwareSystem1, "Uses from container 2 to system 1", "REST")
        softwareSystem1.uses(backend2, "Uses from system 1 to container 2", "REST")

        val viewModel = SoftwareSystemDependenciesPageViewModel(generatorContext, softwareSystem1)
        // Inbound Table
        assertThat(viewModel.dependenciesInboundTable.bodyRows.extractTitle())
            .containsExactly("Software system 2")
        // Outbound Table
        assertThat(viewModel.dependenciesOutboundTable.bodyRows.extractTitle())
            .containsExactly("Software system 2")
    }


    @Test
    fun `dependencies from and to external systems`() {
        val externalSystem = generatorContext.workspace.model
            .addSoftwareSystem(Location.External, "External system", "")
        externalSystem.uses(softwareSystem1, "Uses", "REST")
        softwareSystem1.uses(externalSystem, "Uses", "REST")

        val viewModel = SoftwareSystemDependenciesPageViewModel(generatorContext, softwareSystem1)
        assertThat(viewModel.dependenciesInboundTable.bodyRows[0].columns[0])
            .isEqualTo(TableViewModel.TextCellViewModel("External system (External)", isHeader = true, greyText = true))
        assertThat(viewModel.dependenciesOutboundTable.bodyRows[0].columns[0])
            .isEqualTo(TableViewModel.TextCellViewModel("External system (External)", isHeader = true, greyText = true))
    }

    @Test
    fun `sort by source system name case insensitive`() {
        val system = generatorContext.workspace.model.addSoftwareSystem("Software system 3")
        system.uses(softwareSystem1, "Uses", "REST")
        softwareSystem1.uses(softwareSystem2, "Uses REST", "REST")
        softwareSystem2.uses(softwareSystem1, "Uses SOAP", "SOAP")

        val viewModel = SoftwareSystemDependenciesPageViewModel(generatorContext, softwareSystem1)
        // Inbound Table
        assertThat(viewModel.dependenciesInboundTable.bodyRows.extractTitle())
            .containsExactly("Software system 2", "Software system 3")
        // Outbound Table
        assertThat(viewModel.dependenciesInboundTable.bodyRows.extractTitle())
            .containsExactly("Software system 2", "Software system 3")
    }

    private fun TableViewModel.TableViewInitializerContext.dependenciesTableHeader() {
        headerRow(
            headerCell("System"),
            headerCell("Description"),
            headerCell("Technology"),
        )
    }

    private fun List<TableViewModel.RowViewModel>.extractTitle() = map {
        when (val source = it.columns[0]) {
            is TableViewModel.TextCellViewModel -> source.title
            is TableViewModel.LinkCellViewModel -> source.link.title
            is TableViewModel.ExternalLinkCellViewModel -> source.link.title
        }
    }
}
