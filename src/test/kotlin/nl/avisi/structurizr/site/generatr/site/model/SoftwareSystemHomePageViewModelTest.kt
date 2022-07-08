package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test

class SoftwareSystemHomePageViewModelTest : ViewModelTest() {
    @Test
    fun url() {
        val softwareSystem = generatorContext().workspace.model.addSoftwareSystem("Software system With 1 name")
        assertThat(SoftwareSystemHomePageViewModel.url(softwareSystem))
            .isEqualTo("/software-system-with-1-name")
    }
}
