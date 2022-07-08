package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.containsExactly
import kotlin.test.Test

class SoftwareSystemTabBarViewModelTest : ViewModelTest() {
    private val generatorContext = generatorContext()
    private val softwareSystem = generatorContext.workspace.model.addSoftwareSystem("Some system")
    private val pageViewModel = object : PageViewModel(generatorContext) {
        override val url: String = "/page"
        override val pageSubTitle: String = "Page"
    }

    @Test
    fun `no diagrams, dependencies and decisions`() {
        val viewModel = SoftwareSystemTabBarViewModel.create(pageViewModel, softwareSystem)

        assertThat(viewModel.tabs)
            .containsExactly(
                TabViewModel(
                    LinkViewModel(pageViewModel, "Info", SoftwareSystemHomePageViewModel.url(softwareSystem)),
                    true
                )
            )
    }
}
