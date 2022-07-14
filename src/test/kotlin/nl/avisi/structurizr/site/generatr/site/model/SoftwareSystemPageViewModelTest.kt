package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.*
import nl.avisi.structurizr.site.generatr.site.model.SoftwareSystemPageViewModel.Tab
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import kotlin.test.Test

class SoftwareSystemPageViewModelTest : ViewModelTest() {
    @TestFactory
    fun url() = listOf(
        Tab.HOME to "/software-system-with-1-name",
        Tab.SYSTEM_CONTEXT to "/software-system-with-1-name/context",
    ).map { (tab, expectedUrl) ->
        DynamicTest.dynamicTest("url - $tab") {
            val generatorContext = generatorContext()
            val softwareSystem = generatorContext.workspace.model.addSoftwareSystem("Software system With 1 name")

            assertThat(SoftwareSystemPageViewModel.url(softwareSystem, tab))
                .isEqualTo(expectedUrl)
            assertThat(SoftwareSystemPageViewModel(generatorContext, softwareSystem, tab).url)
                .isEqualTo(SoftwareSystemPageViewModel.url(softwareSystem, tab))
        }
    }

    @TestFactory
    fun subtitle() = Tab.values().map { tab ->
        DynamicTest.dynamicTest("subtitle - $tab") {
            val generatorContext = generatorContext()
            val softwareSystem = generatorContext.workspace.model.addSoftwareSystem("Some software system")

            assertThat(SoftwareSystemPageViewModel(generatorContext, softwareSystem, tab).pageSubTitle)
                .isEqualTo("Some software system")
        }
    }

    @Test
    fun `software system description`() {
        val generatorContext = generatorContext()
        val softwareSystem = generatorContext.workspace.model.addSoftwareSystem("System", "Description")
        val viewModel = SoftwareSystemPageViewModel(generatorContext, softwareSystem, Tab.HOME)

        assertThat(viewModel.description).isEqualTo("Description")
    }

    @Test
    fun tabs() {
        val generatorContext = generatorContext()
        val softwareSystem = generatorContext.workspace.model.addSoftwareSystem("Some software system")
        val viewModel = SoftwareSystemPageViewModel(generatorContext, softwareSystem, Tab.HOME)

        assertThat(viewModel.tabs.map { it.tab })
            .containsExactly(Tab.HOME, Tab.SYSTEM_CONTEXT)
    }

    @TestFactory
    fun `active tab`() = Tab.values().map { tab ->
        DynamicTest.dynamicTest("active tab - $tab") {
            val generatorContext = generatorContext()
            val softwareSystem = generatorContext.workspace.model.addSoftwareSystem("Some software system")
            val viewModel = SoftwareSystemPageViewModel(generatorContext, softwareSystem, tab)

            assertThat(
                viewModel.tabs
                    .single { it.link.active }
                    .tab
            ).isEqualTo(tab)
        }
    }

    @Test
    fun `software system tab only visible when context diagrams available`() {
        val generatorContext = generatorContext()
        val softwareSystem = generatorContext.workspace.model.addSoftwareSystem("Some software system")
        val viewModel = SoftwareSystemPageViewModel(generatorContext, softwareSystem, Tab.HOME)

        assertThat(getTab(viewModel, Tab.SYSTEM_CONTEXT).visible).isFalse()
        generatorContext.workspace.views.createSystemContextView(softwareSystem, "context", "description")
        assertThat(getTab(viewModel, Tab.SYSTEM_CONTEXT).visible).isTrue()
    }

    private fun getTab(viewModel: SoftwareSystemPageViewModel, tab: Tab) =
        viewModel.tabs.single { it.tab == tab }
}
