package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.containsExactly
import com.structurizr.model.SoftwareSystem
import kotlin.test.Test

class SoftwareSystemContextPageViewModelTest : ViewModelTest() {
    private val generatorContext = generatorContext()
    private val softwareSystem: SoftwareSystem = generatorContext.workspace.model
        .addSoftwareSystem("Software system").also {
            generatorContext.workspace.views.createSystemContextView(it, "context-1", "System context view 1")
            generatorContext.workspace.views.createSystemContextView(it, "context-2", "System context view 2")
        }

    @Test
    fun `diagrams sorted by key`() {
        val viewModel = SoftwareSystemContextPageViewModel(generatorContext, softwareSystem)

        assertThat(viewModel.diagrams).containsExactly(
            DiagramViewModel(
                "Software system - System Context",
                ImageViewModel(viewModel, "/svg/context-1.svg"),
                ImageViewModel(viewModel, "/png/context-1.png"),
                ImageViewModel(viewModel, "/puml/context-1.puml")
            ),
            DiagramViewModel(
                "Software system - System Context",
                ImageViewModel(viewModel, "/svg/context-2.svg"),
                ImageViewModel(viewModel, "/png/context-2.png"),
                ImageViewModel(viewModel, "/puml/context-2.puml")
            )
        )
    }
}
