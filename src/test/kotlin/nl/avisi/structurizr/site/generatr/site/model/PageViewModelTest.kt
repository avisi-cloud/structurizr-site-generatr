package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

class PageViewModelTest : ViewModelTest() {

    @Test
    fun `page title`() {
        assertThat(pageViewModel().pageTitle).isEqualTo("Some page | Workspace name")
    }
}
