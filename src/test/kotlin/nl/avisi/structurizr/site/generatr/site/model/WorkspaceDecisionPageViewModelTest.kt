package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.structurizr.documentation.Decision
import kotlin.test.Test

class WorkspaceDecisionPageViewModelTest : ViewModelTest() {
    @Test
    fun url() {
        assertThat(WorkspaceDecisionPageViewModel.url(Decision("1")))
            .isEqualTo("/decisions/1")
    }
}
