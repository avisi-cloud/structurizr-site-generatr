package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

class CustomStylesheetViewModelTest : ViewModelTest() {
    private val generatorContext = generatorContext()
    private val viewModel = pageViewModel()

    @Test
    fun `no custom stylesheet`() {
        val customStylesheetViewModel = CustomStylesheetViewModel(generatorContext, viewModel)
        assertThat(customStylesheetViewModel.resourceURI).isEqualTo(null)
    }

    @Test
    fun `custom stylesheet set to URL`() {
        generatorContext.workspace.views.configuration.addProperty("generatr.style.customStylesheet","http://example.uri/custom.css")
        val customStylesheetViewModel = CustomStylesheetViewModel(generatorContext, viewModel)
        assertThat(customStylesheetViewModel.resourceURI).isEqualTo("http://example.uri/custom.css")
    }

    @Test
    fun `custom stylesheet set to local asset`() {
        generatorContext.workspace.views.configuration.addProperty("generatr.style.customStylesheet","site/custom.css")
        val customStylesheetViewModel = CustomStylesheetViewModel(generatorContext, viewModel)
        assertThat(customStylesheetViewModel.resourceURI).isEqualTo("../site/custom.css")
    }
}
