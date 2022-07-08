package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test

class SoftwareSystemHomePageViewModelTest : ViewModelTest() {
    @Test
    fun url() {
        val generatorContext = generatorContext()
        val softwareSystem = generatorContext.workspace.model.addSoftwareSystem("Software system With 1 name")

        assertThat(SoftwareSystemHomePageViewModel.url(softwareSystem))
            .isEqualTo("/software-system-with-1-name")
        assertThat(SoftwareSystemHomePageViewModel(generatorContext, softwareSystem).url)
            .isEqualTo(SoftwareSystemHomePageViewModel.url(softwareSystem))
    }

    @Test
    fun subtitle() {
        val generatorContext = generatorContext()
        val softwareSystem = generatorContext.workspace.model.addSoftwareSystem("Some software system")

        assertThat(SoftwareSystemHomePageViewModel(generatorContext, softwareSystem).pageSubTitle)
            .isEqualTo("Some software system")
    }

    @Test
    fun `tab bar`() {
        val generatorContext = generatorContext()
        val softwareSystem = generatorContext.workspace.model.addSoftwareSystem("Some software system")
        val viewModel = SoftwareSystemHomePageViewModel(generatorContext, softwareSystem)

        assertThat(viewModel.tabBar)
            .isEqualTo(SoftwareSystemTabBarViewModel.create(viewModel, softwareSystem))
    }

    @Test
    fun `software system description`() {
        val generatorContext = generatorContext()
        val softwareSystem = generatorContext.workspace.model.addSoftwareSystem("System", "Description")
        val viewModel = SoftwareSystemHomePageViewModel(generatorContext, softwareSystem)

        assertThat(viewModel.description)
            .isEqualTo("Description")
    }
}
