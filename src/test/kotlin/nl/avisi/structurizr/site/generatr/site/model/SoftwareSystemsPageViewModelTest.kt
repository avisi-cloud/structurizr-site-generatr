package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test

class SoftwareSystemsPageViewModelTest {
    @Test
    fun url() {
        assertThat(SoftwareSystemsPageViewModel.url())
            .isEqualTo("/software-systems")
    }
}
