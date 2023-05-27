package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test

class ContainerDecisionsTableViewModelTest : ViewModelTest() {

    @Test
    fun `no container with decisions available`() {
        assertThat(pageViewModel().createContainerDecisionsTableViewModel(emptySet()) { "href" })
            .isEqualTo(
                TableViewModel.create {
                    containersTableHeaderRow()
                }
            )
    }

    @Test
    fun `many containers shown if they have decisions`() {
        val containers = generatorContext().workspace.model.addSoftwareSystem("Mock").also {
            it.addContainer("Web Application")
            it.addContainer("API Application")
                    .documentation.addDecision(createDecision("1","API Decision"))
            it.addContainer("Mobile Application")
                    .documentation.addDecision(createDecision("1", "Mobile Decision"))
        }.containers
        val pageViewModel = pageViewModel()
        assertThat(pageViewModel.createContainerDecisionsTableViewModel(containers) { it.name }).isEqualTo(
            TableViewModel.create {
                containersTableHeaderRow()
                bodyRow(
                    cellWithIndex("1"),
                    cellWithLink(pageViewModel, "API Application", "API Application"),
                )
                bodyRow(
                    cellWithIndex("2"),
                    cellWithLink(pageViewModel, "Mobile Application", "Mobile Application")
                )
            }
        )
    }

    private fun TableViewModel.TableViewInitializerContext.containersTableHeaderRow() {
        headerRow(headerCell("#"), headerCell("Container Decisions"))
    }
}
