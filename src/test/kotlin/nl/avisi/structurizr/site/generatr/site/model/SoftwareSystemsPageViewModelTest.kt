package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEqualTo
import kotlin.test.Test

class SoftwareSystemsPageViewModelTest : ViewModelTest() {
    @Test
    fun url() {
        assertThat(SoftwareSystemsPageViewModel.url())
            .isEqualTo("/software-systems")
        assertThat(SoftwareSystemsPageViewModel(generatorContext()).url)
            .isEqualTo(SoftwareSystemsPageViewModel.url())
    }

    @Test
    fun subtitle() {
        assertThat(SoftwareSystemsPageViewModel(generatorContext()).pageSubTitle)
            .isEqualTo("Software Systems")
    }

    @Test
    fun `software systems table`() {
        val generatorContext = generatorContext()
        val system1 = generatorContext.workspace.model.addSoftwareSystem("System 1", "System 1 description")
        val system2 = generatorContext.workspace.model.addSoftwareSystem("System 2", "System 2 description")
        val viewModel = SoftwareSystemsPageViewModel(generatorContext)

        assertThat(viewModel.softwareSystemsTable).isEqualTo(
            TableViewModel.create {
                headerRow(headerCell("Name"), headerCell("Description"))
                bodyRow(
                    headerCellWithLink(
                        viewModel, system1.name, SoftwareSystemPageViewModel.url(
                            system1,
                            SoftwareSystemPageViewModel.Tab.HOME
                        )
                    ),
                    cell(system1.description)
                )
                bodyRow(
                    headerCellWithLink(
                        viewModel, system2.name, SoftwareSystemPageViewModel.url(
                            system2,
                            SoftwareSystemPageViewModel.Tab.HOME
                        )
                    ),
                    cell(system2.description)
                )
            }
        )
    }

    @Test
    fun `software systems table sorted by name - case insensitive`() {
        val generatorContext = generatorContext()
        val system1 = generatorContext.workspace.model.addSoftwareSystem("system 1", "System 1 description")
        val system2 = generatorContext.workspace.model.addSoftwareSystem("System 2", "System 2 description")
        val viewModel = SoftwareSystemsPageViewModel(generatorContext)

        val names = viewModel.softwareSystemsTable.bodyRows
            .map { it.columns[0] }
            .filterIsInstance<TableViewModel.LinkCellViewModel>()
            .map { it.link.title }

        assertThat(names).containsExactly(system1.name, system2.name)
    }

    @Test
    fun `external systems have grey text (declared external by tag)`() {
        val generatorContext = generatorContext()
        generatorContext.workspace.views.configuration.addProperty("generatr.site.externalTag", "External System")
        generatorContext.workspace.model.addSoftwareSystem("system 1", "System 1 description")
        generatorContext.workspace.model.addSoftwareSystem("system 2", "System 2 description").apply { addTags("External System") }

        val viewModel = SoftwareSystemsPageViewModel(generatorContext)

        assertThat(viewModel.softwareSystemsTable.bodyRows[1].columns[0])
            .isEqualTo(TableViewModel.TextCellViewModel("system 2 (External)", isHeader = true, greyText = true))
    }
}
