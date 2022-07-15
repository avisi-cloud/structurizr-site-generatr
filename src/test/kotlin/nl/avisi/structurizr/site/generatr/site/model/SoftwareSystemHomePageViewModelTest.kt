package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.structurizr.model.SoftwareSystem
import kotlin.test.Test

class SoftwareSystemHomePageViewModelTest : ViewModelTest() {
    @Test
    fun `active tab`() {
        val generatorContext = generatorContext()
        val softwareSystem: SoftwareSystem = generatorContext.workspace.model.addSoftwareSystem("Software system")
        val viewModel = SoftwareSystemHomePageViewModel(generatorContext, softwareSystem)

        assertThat(viewModel.tabs.single { it.link.active }.tab)
            .isEqualTo(SoftwareSystemPageViewModel.Tab.HOME)
    }
}
