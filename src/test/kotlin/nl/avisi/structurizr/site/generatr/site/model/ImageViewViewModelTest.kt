package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.structurizr.model.SoftwareSystem
import kotlin.test.Test

class ImageViewViewModelTest : ViewModelTest() {
    private val generatorContext = generatorContext()
    private val softwareSystem: SoftwareSystem = generatorContext.workspace.model.addSoftwareSystem("Software system 1")
    private val container = softwareSystem.addContainer("Container 1")
    private val component = container.addComponent("Component 1")

    @Test
    fun `image name for software system`() {
        val imageView = ImageViewViewModel(createImageView(generatorContext.workspace, softwareSystem))
        assertThat(imageView.name).isEqualTo("Software system 1 - Containers")
    }

    @Test
    fun `image name for container`() {
        val imageView = ImageViewViewModel(createImageView(generatorContext.workspace, container))
        assertThat(imageView.name).isEqualTo("Software system 1 - Container 1 - Components")
    }

    @Test
    fun `image name for component`() {
        val imageView = ImageViewViewModel(createImageView(generatorContext.workspace, component))
        assertThat(imageView.name).isEqualTo("Software system 1 - Container 1 - Component 1 - Code")
    }
}
