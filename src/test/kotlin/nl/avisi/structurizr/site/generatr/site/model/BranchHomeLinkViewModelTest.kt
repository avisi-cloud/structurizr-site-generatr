package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test

class BranchHomeLinkViewModelTest : ViewModelTest() {
    private val pageViewModel = object : PageViewModel(generatorContext()) {
        override val url: String = "/master/decisions/1"
        override val pageSubTitle: String = "subtitle"
    }

    @Test
    fun `title is branch name`() {
        val viewModel = BranchHomeLinkViewModel(pageViewModel, "branch-1")

        assertThat(viewModel.title)
            .isEqualTo("branch-1")
    }

    @Test
    fun `relative href to`() {
        val viewModel = BranchHomeLinkViewModel(pageViewModel, "branch-1")

        assertThat(viewModel.relativeHref)
            .isEqualTo("../../../../branch-1")
    }
}
