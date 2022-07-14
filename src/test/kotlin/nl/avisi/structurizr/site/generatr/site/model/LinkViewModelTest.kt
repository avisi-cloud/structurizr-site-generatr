package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import kotlin.test.Test

class LinkViewModelTest : ViewModelTest() {
    @Test
    fun `exact links are active when the page url matches exactly`() {
        val pageViewModel = pageViewModel("/some-page")
        val viewModel = LinkViewModel(pageViewModel, "Some page", "/some-page")
        assertThat(viewModel.active).isTrue()
    }

    @ParameterizedTest
    @ValueSource(strings = ["/some-page/1", "/", "/some-other-page"])
    fun `exact links are not active when the page url matches exactly`(href: String) {
        val pageViewModel = pageViewModel("/some-page")
        val viewModel = LinkViewModel(pageViewModel, "Some page", href)
        assertThat(viewModel.active).isFalse()
    }

    @ParameterizedTest
    @ValueSource(strings = ["/some-page", "/some-page/1", "/some-page/subpage/subsubpage"])
    fun `non-exact links are active when the page url matches partially`(pageHref: String) {
        val pageViewModel = pageViewModel(pageHref)
        val viewModel = LinkViewModel(pageViewModel, "Some page", "/some-page", false)
        assertThat(viewModel.active).isTrue()
    }

    @ParameterizedTest
    @ValueSource(strings = ["/", "/some-other-page/1", "/some-other-page/subpage/subsubpage"])
    fun `non-exact links are not active when the page url doesn't match partially`(pageHref: String) {
        val pageViewModel = pageViewModel(pageHref)
        val viewModel = LinkViewModel(pageViewModel, "Some page", "/some-page", false)
        assertThat(viewModel.active).isFalse()
    }

    @Test
    fun `relative href`() {
        val pageViewModel = pageViewModel("/some-page/some-subpage")
        val viewModel = LinkViewModel(pageViewModel, "Some other page", "/some-other-page")
        assertThat(viewModel.relativeHref).isEqualTo("../../some-other-page")
    }
}
