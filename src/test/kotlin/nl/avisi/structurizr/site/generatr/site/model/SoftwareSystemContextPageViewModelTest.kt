package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isFalse
import assertk.assertions.isTrue
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
                "context-1",
                "Software system - System Context",
                "System context view 1",
                """<svg viewBox="0 0 800 900"></svg>""",
                800,
                ImageViewModel(viewModel, "/svg/context-1.svg"),
                ImageViewModel(viewModel, "/png/context-1.png"),
                ImageViewModel(viewModel, "/puml/context-1.puml"),
                LegendViewModel(
                    """<svg viewBox="0 0 800 900"></svg>""",
                    800,
                    ImageViewModel(viewModel, "/svg/context-1.legend.svg"),
                    ImageViewModel(viewModel, "/png/context-1.legend.png"),
                    ImageViewModel(viewModel, "/puml/context-1.legend.puml"),
                )
            ),
            DiagramViewModel(
                "context-2",
                "Software system - System Context",
                "System context view 2",
                """<svg viewBox="0 0 800 900"></svg>""",
                800,
                ImageViewModel(viewModel, "/svg/context-2.svg"),
                ImageViewModel(viewModel, "/png/context-2.png"),
                ImageViewModel(viewModel, "/puml/context-2.puml"),
                LegendViewModel(
                    """<svg viewBox="0 0 800 900"></svg>""",
                    800,
                    ImageViewModel(viewModel, "/svg/context-2.legend.svg"),
                    ImageViewModel(viewModel, "/png/context-2.legend.png"),
                    ImageViewModel(viewModel, "/puml/context-2.legend.puml"),
                )
            )
        )
    }

    @Test
    fun `hidden view`() {
        val viewModel = SoftwareSystemContextPageViewModel(
            generatorContext,
            generatorContext.workspace.model.addSoftwareSystem("Software system 2")
        )

        assertThat(viewModel.visible).isFalse()
    }

    @Test
    fun `no index`() {
        val viewModel = SoftwareSystemContextPageViewModel(generatorContext, generatorContext.workspace.model
            .addSoftwareSystem("Software system 2").also {
                generatorContext.workspace.views.createSystemContextView(it, "context-3", "System context view 3")
            })
        assertThat(viewModel.diagramIndex.visible).isFalse()
    }

    @Test
    fun `has index`() {
        val viewModel = SoftwareSystemContextPageViewModel(generatorContext, softwareSystem)
        assertThat(viewModel.diagramIndex.visible).isTrue()
    }
}
