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
                    headerCellWithLink(viewModel, system1.name, SoftwareSystemHomePageViewModel.url(system1)),
                    cell(system1.description)
                )
                bodyRow(
                    headerCellWithLink(viewModel, system2.name, SoftwareSystemHomePageViewModel.url(system2)),
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

        assertThat(names)
            .containsExactly(system1.name, system2.name)
    }
}
