package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEqualTo
import kotlin.test.Test

class HeaderBarViewModelTest : ViewModelTest() {
    private val generatorContext = generatorContext(
        "some workspace", branches = listOf("main", "branch-2"), version = "0.42.1337"
    )
    private val pageViewModel = object : PageViewModel(generatorContext) {
        override val url: String = "/master/system"
        override val pageSubTitle: String = "subtitle"
    }

    @Test
    fun `workspace title link`() {
        val viewModel = HeaderBarViewModel(pageViewModel, generatorContext)

        assertThat(viewModel.titleLink).isEqualTo(
            LinkViewModel(pageViewModel, "some workspace", "/")
        )
    }

    @Test
    fun `current branch`() {
        val viewModel = HeaderBarViewModel(pageViewModel, generatorContext)

        assertThat(viewModel.currentBranch).isEqualTo(generatorContext.currentBranch)
    }

    @Test
    fun `branch pulldown menu`() {
        val viewModel = HeaderBarViewModel(pageViewModel, generatorContext)

        assertThat(viewModel.branches).containsExactly(
            LinkViewModel(pageViewModel, "main", "/main/"),
            LinkViewModel(pageViewModel, "branch-2", "/branch-2/")
        )
    }

    @Test
    fun `version number`() {
        val viewModel = HeaderBarViewModel(pageViewModel, generatorContext)

        assertThat(viewModel.version).isEqualTo("0.42.1337")
    }
}
