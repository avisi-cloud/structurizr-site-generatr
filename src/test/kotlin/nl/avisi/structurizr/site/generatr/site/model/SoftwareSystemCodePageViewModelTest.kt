package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.structurizr.model.SoftwareSystem
import org.junit.jupiter.api.Test

class SoftwareSystemCodePageViewModelTest : ViewModelTest() {
    private val generatorContext = generatorContext()
    private val softwareSystem: SoftwareSystem = generatorContext.workspace.model.addSoftwareSystem("Software system")
    private val container = softwareSystem.addContainer("container")
    private val component = container.addComponent("component")
    private val imageView = createImageView(generatorContext.workspace, component)

    @Test
    fun `active tab`() {
        val viewModel = SoftwareSystemCodePageViewModel(generatorContext, softwareSystem)

        assertThat(viewModel.tabs.single { it.link.active }.tab)
            .isEqualTo(SoftwareSystemPageViewModel.Tab.CODE)
    }

    @Test
    fun `has image`() {
        val viewModel = SoftwareSystemCodePageViewModel(generatorContext, softwareSystem)

        assertThat(viewModel.visible).isTrue()
        assertThat(viewModel.images).hasSize(1)
        assertThat(viewModel.images.single().imageView).isEqualTo(imageView)
    }

    @Test
    fun `hidden view`() {
        val viewModel = SoftwareSystemCodePageViewModel(
            generatorContext,
            generatorContext.workspace.model.addSoftwareSystem("Software system 2").apply {
                addContainer("container").apply {
                    addComponent("component")
                }
            }
        )

        assertThat(viewModel.visible).isFalse()
    }
}
