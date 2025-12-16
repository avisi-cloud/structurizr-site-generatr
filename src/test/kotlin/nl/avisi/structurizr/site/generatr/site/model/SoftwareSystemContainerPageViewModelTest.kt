package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.structurizr.model.SoftwareSystem
import kotlin.test.Test

class SoftwareSystemContainerPageViewModelTest : ViewModelTest() {
    private val generatorContext = generatorContext()
    private val softwareSystem: SoftwareSystem = generatorContext.workspace.model
        .addSoftwareSystem("Software system").also {
            generatorContext.workspace.views.createContainerView(it, "container-1", "Container view 1")
            generatorContext.workspace.views.createContainerView(it, "container-2", "Container view 2")
        }
    private val imageView = createImageView(generatorContext.workspace, softwareSystem)

    @Test
    fun `active tab`() {
        val viewModel = SoftwareSystemContainerPageViewModel(generatorContext, softwareSystem)

        assertThat(viewModel.tabs.single { it.link.active }.tab)
            .isEqualTo(SoftwareSystemPageViewModel.Tab.CONTAINER)
    }

    @Test
    fun `diagrams sorted by key`() {
        val viewModel = SoftwareSystemContainerPageViewModel(generatorContext, softwareSystem)

        assertThat(viewModel.visible).isTrue()
        assertThat(viewModel.diagrams).containsExactly(
            DiagramViewModel(
                "container-1",
                "Software system - Containers",
                "Container view 1",
                """<svg viewBox="0 0 800 900"></svg>""",
                800,
                ImageViewModel(viewModel, "/svg/container-1.svg"),
                ImageViewModel(viewModel, "/png/container-1.png"),
                ImageViewModel(viewModel, "/puml/container-1.puml"),
                LegendViewModel(
                    """<svg viewBox="0 0 800 900"></svg>""",
                    800,
                    ImageViewModel(viewModel, "/svg/container-1.legend.svg"),
                    ImageViewModel(viewModel, "/png/container-1.legend.png"),
                    ImageViewModel(viewModel, "/puml/container-1.legend.puml"),
                )
            ),
            DiagramViewModel(
                "container-2",
                "Software system - Containers",
                "Container view 2",
                """<svg viewBox="0 0 800 900"></svg>""",
                800,
                ImageViewModel(viewModel, "/svg/container-2.svg"),
                ImageViewModel(viewModel, "/png/container-2.png"),
                ImageViewModel(viewModel, "/puml/container-2.puml"),
                LegendViewModel(
                    """<svg viewBox="0 0 800 900"></svg>""",
                    800,
                    ImageViewModel(viewModel, "/svg/container-2.legend.svg"),
                    ImageViewModel(viewModel, "/png/container-2.legend.png"),
                    ImageViewModel(viewModel, "/puml/container-2.legend.puml"),
                )
            )
        )
    }

    @Test
    fun `has image`() {
        val viewModel = SoftwareSystemContainerPageViewModel(generatorContext, softwareSystem)

        assertThat(viewModel.visible).isTrue()
        assertThat(viewModel.images).hasSize(1)
        assertThat(viewModel.images.single().imageView).isEqualTo(imageView)
    }

    @Test
    fun `hidden view`() {
        val viewModel = SoftwareSystemContainerPageViewModel(
            generatorContext,
            generatorContext.workspace.model.addSoftwareSystem("Software system 2")
        )

        assertThat(viewModel.visible).isFalse()
    }

    @Test
    fun `no index`() {
        val viewModel = SoftwareSystemContainerPageViewModel(
            generatorContext,
            generatorContext.workspace.model.addSoftwareSystem("Software system 2").also {
                generatorContext.workspace.views.createContainerView(it, "container-3", "Container view 3")
            }
        )
        assertThat(viewModel.diagramIndex.visible).isFalse()
    }

    @Test
    fun `has index`() {
        val viewModel = SoftwareSystemContainerPageViewModel(generatorContext, softwareSystem)
        assertThat(viewModel.diagramIndex.visible).isTrue()
    }
}
