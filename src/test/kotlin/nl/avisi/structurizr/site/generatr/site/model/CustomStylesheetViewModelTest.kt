package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

class CustomStylesheetViewModelTest : ViewModelTest() {
    private val generatorContext = generatorContext()

    @Test
    fun `Configuration Property Missing`() {
        val customStylesheetViewModel = CustomStylesheetViewModel(generatorContext)
        assertThat(customStylesheetViewModel.resourceURI).isEqualTo(null)
    }

    @Test
    fun `Configuration Property Set To URL`() {
        generatorContext.workspace.views.configuration.addProperty("generatr.style.customStylesheet","http://example.uri/custom.css")
        val customStylesheetViewModel = CustomStylesheetViewModel(generatorContext)
        assertThat(customStylesheetViewModel.resourceURI).isEqualTo("http://example.uri/custom.css")
    }

    @Test
    fun `Configuration Property Set To Local File`() {
        generatorContext.workspace.views.configuration.addProperty("generatr.style.customStylesheet","custom.css")
        val customStylesheetViewModel = CustomStylesheetViewModel(generatorContext)
        assertThat(customStylesheetViewModel.resourceURI).isEqualTo("./custom.css")
    }
}
