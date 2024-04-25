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
        val viewModel = LinkViewModel(pageViewModel, "Some page", "/some-page", Match.CHILD)
        assertThat(viewModel.active).isTrue()
    }

    @ParameterizedTest
    @ValueSource(strings = ["/", "/some-other-page/1", "/some-other-page/subpage/subsubpage"])
    fun `non-exact links are not active when the page url doesn't match partially`(pageHref: String) {
        val pageViewModel = pageViewModel(pageHref)
        val viewModel = LinkViewModel(pageViewModel, "Some page", "/some-page", Match.CHILD)
        assertThat(viewModel.active).isFalse()
    }

    @Test
    fun `non-exact links are only active when page url is a subdirectory of the link`() {
        val expectInactivePartialMatch = LinkViewModel(pageViewModel("/page-two"), "Some page", "/page", Match.CHILD)
        val expectActivePartialMatch = LinkViewModel(pageViewModel("/page/two"), "Some page", "/page", Match.CHILD)
        assertThat(expectInactivePartialMatch.active).isFalse()
        assertThat(expectActivePartialMatch.active).isTrue()
    }

    @Test
    fun `relative href`() {
        val pageViewModel = pageViewModel("/some-page/some-subpage")
        val viewModel = LinkViewModel(pageViewModel, "Some other page", "/some-other-page")
        assertThat(viewModel.relativeHref).isEqualTo("../../some-other-page/")
    }

    @ParameterizedTest
    @ValueSource(strings = ["/some-page/sibling-page-1/", "/some-page/sibling-page-2/"])
    fun `sibling links are active when the previous url path matches`(pageHref: String) {
        val pageViewModel = pageViewModel(pageHref)
        val viewModel = LinkViewModel(pageViewModel, "Some page", "/some-page/sibling-page-3/", Match.SIBLING)
        assertThat(viewModel.active).isTrue()
    }

    @ParameterizedTest
    @ValueSource(strings = ["/some-other-page/sibling-page-1/", "/some-other-page/sibling-page-2/"])
    fun `sibling links are not active when the previous url path doesn't match`(pageHref: String) {
        val pageViewModel = pageViewModel(pageHref)
        val viewModel = LinkViewModel(pageViewModel, "Some page", "/some-page/sibling-page-1", Match.SIBLING)
        assertThat(viewModel.active).isFalse()
    }

    @Test
    fun `sibling links are only active when previous page url path matches previous href path url`() {
        val expectInactiveSiblingMatch = LinkViewModel(pageViewModel("/page/two/"), "Some page", "/some-page/", Match.SIBLING)
        val expectActiveSiblingMatch = LinkViewModel(pageViewModel("/page/two"), "Some page", "/page/one", Match.SIBLING)
        assertThat(expectInactiveSiblingMatch.active).isFalse()
        assertThat(expectActiveSiblingMatch.active).isTrue()
    }
}
