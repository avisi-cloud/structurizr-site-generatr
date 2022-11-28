package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEqualTo
import com.structurizr.model.SoftwareSystem
import kotlin.test.Test

class SoftwareSystemContainerPageViewModelTest : ViewModelTest() {
    private val generatorContext = generatorContext()
    private val softwareSystem: SoftwareSystem = generatorContext.workspace.model
        .addSoftwareSystem("Software system").also {
            generatorContext.workspace.views.createContainerView(it, "container-1", "Container view 1")
            generatorContext.workspace.views.createContainerView(it, "container-2", "Container view 2")
        }

    @Test
    fun `active tab`() {
        val viewModel = SoftwareSystemContainerPageViewModel(generatorContext, softwareSystem)

        assertThat(viewModel.tabs.single { it.link.active }.tab)
            .isEqualTo(SoftwareSystemPageViewModel.Tab.CONTAINER)
    }

    @Test
    fun `diagrams sorted by key`() {
        val viewModel = SoftwareSystemContainerPageViewModel(generatorContext, softwareSystem)

        assertThat(viewModel.diagrams).containsExactly(
            DiagramViewModel(
                "container-1",
                "Software system - Containers",
                """<svg viewBox="0 0 800 900"></svg>""",
                800,
                ImageViewModel(viewModel, "/svg/container-1.svg"),
                ImageViewModel(viewModel, "/png/container-1.png"),
                ImageViewModel(viewModel, "/puml/container-1.puml")
            ),
            DiagramViewModel(
                "container-2",
                "Software system - Containers",
                """<svg viewBox="0 0 800 900"></svg>""",
                800,
                ImageViewModel(viewModel, "/svg/container-2.svg"),
                ImageViewModel(viewModel, "/png/container-2.png"),
                ImageViewModel(viewModel, "/puml/container-2.puml")
            )
        )
    }
}
