package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

class MarkdownToHtmlTest : ViewModelTest() {

    @Test
    fun `translates markdown`() {
        val generatorContext = generatorContext()
        val viewModel = HomePageViewModel(generatorContext)

        val html = markdownToHtml(
            viewModel,
            """
                ## header
                content
            """.trimIndent(),
            svgFactory
        )

        assertThat(html).isEqualTo(
            """
                <h2>header</h2>
                <p>content</p>
            """.trimIndent()
        )
    }

    @Test
    fun `embedded diagram`() {
        val generatorContext = generatorContext()
        val viewModel = HomePageViewModel(generatorContext)

        val html = markdownToHtml(viewModel, "![System Landscape Diagram](embed:SystemLandscape)", svgFactory)

        assertThat(html).isEqualTo(
            """
                <p>
                 <div>
                  <figure style="width: min(100%, 800px);">
                   <div>
                    <svg viewbox="0 0 800 900"></svg>
                   </div>
                   <figcaption>
                    System Landscape Diagram [<a href="svg/SystemLandscape.svg">svg</a>|<a href="png/SystemLandscape.png">png</a>|<a href="puml/SystemLandscape.puml">puml</a>]
                   </figcaption>
                  </figure>
                 </div></p>
            """.trimIndent()
        )
    }

    @Test
    fun `embedded diagram key not found`() {
        val generatorContext = generatorContext()
        val viewModel = HomePageViewModel(generatorContext)

        val html = markdownToHtml(viewModel, "![Diagram](embed:non-existing)") { _, _ ->
            null
        }

        assertThat(html).isEqualTo(
            """
                <p>
                 <div>
                  <div class="notification is-danger">
                   No view with key<span class="has-text-weight-bold"> non-existing </span>found!
                  </div>
                 </div></p>
            """.trimIndent()
        )
    }
}
