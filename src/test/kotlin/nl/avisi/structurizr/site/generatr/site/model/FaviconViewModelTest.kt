package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.*
import kotlin.test.Test

class FaviconViewModelTest : ViewModelTest() {
    private val generatorContext = generatorContext("some workspace")

    @Test
    fun favicon() {
        generatorContext.workspace.views.configuration.addProperty(
            "generatr.style.faviconPath",
            "site/favicon.png"
        )
        val faviconViewModel = faviconViewModel()

        assertThat(faviconViewModel.includeFavicon).isTrue()
        assertThat(faviconViewModel.type).isEqualTo("image/png")
        assertThat(faviconViewModel.url).isEqualTo("../../site/favicon.png")
    }

    @Test
    fun `invalid favicon`() {
        generatorContext.workspace.views.configuration.addProperty(
            "generatr.style.faviconPath",
            "favicon"
        )

        assertFailure { faviconViewModel() }.hasMessage("Favicon must be a valid *.ico, *.png of *.gif file")
    }

    @Test
    fun `no favicon`() {
        val faviconViewModel = faviconViewModel()

        assertThat(faviconViewModel.includeFavicon).isFalse()
    }

    private fun faviconViewModel() = object : PageViewModel(generatorContext) {
        override val url: String = "/master/system"
        override val pageSubTitle: String = "subtitle"
    }.favicon
}
