package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
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
            LinkViewModel(pageViewModel, "some workspace", HomePageViewModel.url())
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
            BranchHomeLinkViewModel(pageViewModel, "main"),
            BranchHomeLinkViewModel(pageViewModel, "branch-2")
        )
    }

    @Test
    fun `version number`() {
        val viewModel = HeaderBarViewModel(pageViewModel, generatorContext)

        assertThat(viewModel.version).isEqualTo("0.42.1337")
    }

    @Test
    fun logo() {
        generatorContext.workspace.views.configuration.addProperty(
            "generatr.style.logoPath",
            "site/logo.png"
        )
        val viewModel = HeaderBarViewModel(pageViewModel, generatorContext)

        assertThat(viewModel.hasLogo).isTrue()
        assertThat(viewModel.logo).isEqualTo(ImageViewModel(pageViewModel, "/site/logo.png"))
    }

    @Test
    fun `no logo`() {
        val viewModel = HeaderBarViewModel(pageViewModel, generatorContext)

        assertThat(viewModel.hasLogo).isFalse()
    }
}
