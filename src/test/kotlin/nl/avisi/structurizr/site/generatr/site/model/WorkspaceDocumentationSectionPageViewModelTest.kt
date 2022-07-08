package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.structurizr.documentation.Format
import com.structurizr.documentation.Section
import kotlin.test.Test

class WorkspaceDocumentationSectionPageViewModelTest : ViewModelTest() {
    @Test
    fun `normalized url`() {
        val generatorContext = generatorContext()
        val viewModel = WorkspaceDocumentationSectionPageViewModel(
            generatorContext, createSection("Some section With words and 1 number")
        )

        assertThat(viewModel.url).isEqualTo("/some-section-with-words-and-1-number")
    }

    @Test
    fun subtitle() {
        val generatorContext = generatorContext()
        val section = createSection()
        val viewModel = WorkspaceDocumentationSectionPageViewModel(generatorContext, section)

        assertThat(viewModel.pageSubTitle).isEqualTo(section.title)
    }

    @Test
    fun content() {
        val generatorContext = generatorContext()
        val section = createSection()
        val viewModel = WorkspaceDocumentationSectionPageViewModel(generatorContext, section)

        assertThat(viewModel.markdown).isEqualTo(MarkdownViewModel(section.content))
    }

    private fun createSection(title: String = "Section 1") = Section(title, Format.Markdown, "# Content")
}
