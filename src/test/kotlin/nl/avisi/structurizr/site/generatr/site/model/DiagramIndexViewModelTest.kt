package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import kotlin.test.Test

class DiagramIndexViewModelTest : ViewModelTest() {
    private val generatorContext = generatorContext()

    @Test
    fun `index without diagrams` () {
        val viewModel = DiagramIndexViewModel(diagramViewModels())

        assertThat(viewModel.entries).isEqualTo(listOf())
    }

    @Test
    fun `index with diagram` () {
        val softwareSystem = generatorContext.workspace.model.addSoftwareSystem("Software system")
        generatorContext.workspace.views.createSystemContextView(softwareSystem, "system-1", "")
        val viewModel = DiagramIndexViewModel(diagramViewModels())

        assertThat(viewModel.entries).isEqualTo(listOf(IndexEntry("system-1", "System Context View: Software system")))
    }

    @Test
    fun `index with diagram with description` () {
        val softwareSystem = generatorContext.workspace.model.addSoftwareSystem("Software system")
        generatorContext.workspace.views.createSystemContextView(softwareSystem, "system-1", "Some description")
        val viewModel = DiagramIndexViewModel(diagramViewModels())

        assertThat(viewModel.entries).isEqualTo(listOf(IndexEntry("system-1", "System Context View: Software system (Some description)")))
    }

    @Test
    fun `index with exactly one diagram` () {
        val softwareSystem = generatorContext.workspace.model.addSoftwareSystem("Software system")
        generatorContext.workspace.views.createSystemContextView(softwareSystem, "system-1", "")
        val viewModel = DiagramIndexViewModel(diagramViewModels())

        assertThat(viewModel.visible).isFalse()
    }

    @Test
    fun `index with diagram and image with description` () {
        val softwareSystem = generatorContext.workspace.model.addSoftwareSystem("Software system")
        generatorContext.workspace.views.createSystemContextView(softwareSystem, "system-1", "Some description")
        generatorContext.workspace.views.createImageView(softwareSystem, "image-1")
        val viewModel = DiagramIndexViewModel(diagramViewModels(), imageViewViewModels())

        assertThat(viewModel.visible).isTrue()
        assertThat(viewModel.entries).isEqualTo(listOf(
            IndexEntry("system-1", "System Context View: Software system (Some description)"),
            IndexEntry("imageview-2", "Image View Title (Image View Description)")
        ))
    }

    private fun diagramViewModels() = generatorContext.workspace.views.systemContextViews
        .map { DiagramViewModel.forView(this.pageViewModel(), it, generatorContext.svgFactory) }

    private fun imageViewViewModels() = listOf(
        ImageViewViewModel(createImageView(generatorContext.workspace, generatorContext.workspace.model.addSoftwareSystem("Software system2")))
    )
}
