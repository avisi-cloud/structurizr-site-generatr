package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import com.structurizr.model.SoftwareSystem
import kotlin.test.Test

class SoftwareSystemComponentPageViewModelTest : ViewModelTest() {
    private val generatorContext = generatorContext()
    private val softwareSystem: SoftwareSystem = generatorContext.workspace.model
        .addSoftwareSystem("Software system").also {
            val backend = it.addContainer("Backend")
            generatorContext.workspace.views.createComponentView(backend, "component-1", "Component view 1")
            generatorContext.workspace.views.createComponentView(backend, "component-2", "Component view 2")
        }

    @Test
    fun `active tab`() {
        val viewModel = SoftwareSystemComponentPageViewModel(generatorContext, softwareSystem)

        assertThat(viewModel.tabs.single { it.link.active }.tab)
            .isEqualTo(SoftwareSystemPageViewModel.Tab.COMPONENT)
    }

    @Test
    fun `diagrams sorted by key`() {
        val viewModel = SoftwareSystemComponentPageViewModel(generatorContext, softwareSystem)

        assertThat(viewModel.diagrams).containsExactly(
            DiagramViewModel(
                "component-1",
                "Software system - Backend - Components",
                """<svg viewBox="0 0 800 900"></svg>""",
                800,
                ImageViewModel(viewModel, "/svg/component-1.svg"),
                ImageViewModel(viewModel, "/png/component-1.png"),
                ImageViewModel(viewModel, "/puml/component-1.puml")
            ),
            DiagramViewModel(
                "component-2",
                "Software system - Backend - Components",
                """<svg viewBox="0 0 800 900"></svg>""",
                800,
                ImageViewModel(viewModel, "/svg/component-2.svg"),
                ImageViewModel(viewModel, "/png/component-2.png"),
                ImageViewModel(viewModel, "/puml/component-2.puml")
            )
        )
    }

    @Test
    fun `hidden view`() {
        val viewModel = SoftwareSystemComponentPageViewModel(
            generatorContext,
            generatorContext.workspace.model.addSoftwareSystem("Software system 2")
        )

        assertThat(viewModel.diagramsVisible).isFalse()
    }
}
