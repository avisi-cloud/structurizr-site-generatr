package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
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
            .isEqualTo(markdownToHtml(viewModel, DEFAULT_HOMEPAGE_CONTENT, svgFactory))
    }

    @Test
    fun `homepage with workspace docs`() {
        val generatorContext = generatorContext()
        generatorContext.workspace.documentation.addSection(
            createSection("Section content")
        )
        val viewModel = HomePageViewModel(generatorContext)

        assertThat(viewModel.content)
            .isEqualTo(markdownToHtml(viewModel, "Section content", svgFactory))
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
        val generatorContext = generatorContext("Workspace name")
        val viewModel = HomePageViewModel(generatorContext)

        assertThat(viewModel.pageTitle).isEqualTo("Workspace name")
    }

    @Test
    fun `header bar`() {
        val generatorContext = generatorContext("Workspace name")
        val viewModel = HomePageViewModel(generatorContext)

        assertThat(viewModel.headerBar.titleLink.title).isEqualTo("Workspace name")
    }

    @Test
    fun menu() {
        val generatorContext = generatorContext("Workspace name")
        val viewModel = HomePageViewModel(generatorContext)

        assertThat(viewModel.menu.generalItems[0].active).isTrue()
    }
}
