package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

class CustomMermaidInitViewModelTest : ViewModelTest() {
    private val generatorContext = generatorContext()
    private val viewModel = pageViewModel()

    @Test
    fun `no custom init script`() {
        val customInitViewModel = CustomMermaidInitViewModel(generatorContext, viewModel)
        assertThat(customInitViewModel.resourceURI).isEqualTo(null)
    }

    @Test
    fun `custom init set to opaque URL`() {
        generatorContext.workspace.views.configuration.addProperty("generatr.mermaid.customInit","https://example.uri/custom.js")
        val customInitViewModel = CustomMermaidInitViewModel(generatorContext, viewModel)
        assertThat(customInitViewModel.resourceURI).isEqualTo("https://example.uri/custom.js")
    }

    @Test
    fun `custom init set to local asset`() {
        generatorContext.workspace.views.configuration.addProperty("generatr.mermaid.customInit","site/custom.js")
        val customInitViewModel = CustomMermaidInitViewModel(generatorContext, viewModel)
        assertThat(customInitViewModel.resourceURI).isEqualTo("../site/custom.js")
    }
}
