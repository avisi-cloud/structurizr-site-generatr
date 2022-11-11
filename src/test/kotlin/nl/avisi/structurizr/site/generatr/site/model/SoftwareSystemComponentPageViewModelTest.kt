package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEqualTo
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
                "Software system - Backend - Components",
                "<svg></svg>",
                ImageViewModel(viewModel, "/svg/component-1.svg"),
                ImageViewModel(viewModel, "/png/component-1.png"),
                ImageViewModel(viewModel, "/puml/component-1.puml")
            ),
            DiagramViewModel(
                "Software system - Backend - Components",
                "<svg></svg>",
                ImageViewModel(viewModel, "/svg/component-2.svg"),
                ImageViewModel(viewModel, "/png/component-2.png"),
                ImageViewModel(viewModel, "/puml/component-2.puml")
            )
        )
    }
}
