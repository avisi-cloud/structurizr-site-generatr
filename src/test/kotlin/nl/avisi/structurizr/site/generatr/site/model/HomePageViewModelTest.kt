package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import com.structurizr.documentation.Format
import com.structurizr.documentation.Section
import nl.avisi.structurizr.site.generatr.site.model.HomePageViewModel.Companion.DEFAULT_HOMEPAGE_CONTENT
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import kotlin.test.Test

class HomePageViewModelTest : ViewModelTest() {
    @Test
    fun url() {
        assertThat(HomePageViewModel.url())
            .isEqualTo("/")
    }

    @Test
    fun `default homepage`() {
        val generatorContext = generatorContext()
        val viewModel = HomePageViewModel(generatorContext)

        assertThat(viewModel.content)
            .isEqualTo(MarkdownViewModel(DEFAULT_HOMEPAGE_CONTENT))
    }

    @Test
    fun `homepage with workspace docs`() {
        val generatorContext = generatorContext()
        generatorContext.workspace.documentation.addSection(
            Section("Section title", Format.Markdown, "Section content")
        )
        val viewModel = HomePageViewModel(generatorContext)

        assertThat(viewModel.content)
            .isEqualTo(MarkdownViewModel("Section content"))
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "  "])
    fun `page title with blank workspace name`(workspaceName: String) {
        val generatorContext = generatorContext(workspaceName)
        val viewModel = HomePageViewModel(generatorContext)

        assertThat(viewModel.pageTitle).isEqualTo("Home")
    }

    @Test
    fun `page title with workspace name`() {
        val generatorContext = generatorContext("this is a workspace")
        val viewModel = HomePageViewModel(generatorContext)

        assertThat(viewModel.pageTitle).isEqualTo("Home | this is a workspace")
    }

    @Test
    fun `header bar`() {
        val generatorContext = generatorContext("this is a workspace")
        val viewModel = HomePageViewModel(generatorContext)

        assertThat(viewModel.headerBar.titleLink.title).isEqualTo("this is a workspace")
    }

    @Test
    fun menu() {
        val generatorContext = generatorContext("this is a workspace")
        val viewModel = HomePageViewModel(generatorContext)

        assertThat(viewModel.menu.generalItems[0].active).isTrue()
    }
}
