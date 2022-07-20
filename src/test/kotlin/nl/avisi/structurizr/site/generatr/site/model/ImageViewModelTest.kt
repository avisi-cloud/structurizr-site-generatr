package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test

class ImageViewModelTest : ViewModelTest() {
    @Test
    fun `relative href`() {
        val pageViewModel = pageViewModel("/some-page/some-subpage/")
        val viewModel = ImageViewModel(pageViewModel, "/svg/image.svg")

        assertThat(viewModel.relativeHref).isEqualTo("../../svg/image.svg")
    }
}
