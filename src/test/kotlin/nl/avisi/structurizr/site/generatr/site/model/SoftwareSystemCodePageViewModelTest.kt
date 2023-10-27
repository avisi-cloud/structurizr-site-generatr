package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import com.structurizr.model.SoftwareSystem
import kotlin.test.Test

class SoftwareSystemCodePageViewModelTest : ViewModelTest() {
    private val generatorContext = generatorContext()
    private val softwareSystem: SoftwareSystem = generatorContext.workspace.model.addSoftwareSystem("Software system")
    private val container = softwareSystem.addContainer("container")
    private val component = container.addComponent("component")
    private val imageView = generatorContext.workspace.views.createImageView(component, "imageview-001").also {
        it.description = "Image View Description"
        it.title = "Image View Title"
        it.contentType = "image/png"
        it.content = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C8AAAAASUVORK5CYII="
    }

    @Test
    fun `active tab`() {
        val viewModel = SoftwareSystemCodePageViewModel(generatorContext, softwareSystem)

        assertThat(viewModel.tabs.single { it.link.active }.tab)
            .isEqualTo(SoftwareSystemPageViewModel.Tab.CODE)
    }

    @Test
    fun `check code page model has 1 image and it matches the above imageView`() {
        val viewModel = SoftwareSystemCodePageViewModel(generatorContext, softwareSystem)

        assertThat(viewModel.images).hasSize(1)
        assertThat(viewModel.images[0]).isEqualTo(imageView)
    }

    @Test
    fun `hidden view`() {
        val viewModel = SoftwareSystemCodePageViewModel(
            generatorContext,
            generatorContext.workspace.model.addSoftwareSystem("Software system 2")
        )

        assertThat(viewModel.imagesVisible).isFalse()
    }
}
