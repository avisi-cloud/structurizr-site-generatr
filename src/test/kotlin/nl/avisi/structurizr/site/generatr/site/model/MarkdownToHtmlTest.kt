package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import kotlin.random.Random

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
    fun `translates markdown table as default`() {
        val generatorContext = generatorContext()
        val viewModel = HomePageViewModel(generatorContext)

        val html = markdownToHtml(
            viewModel,
            """
                ## header

                | header1 | header2 |
                | ------- | ------- |
                | content | content |
            """.trimIndent(),
            svgFactory
        )

        assertThat(html).isEqualTo(
            """
                <h2>header</h2>
                <table>
                 <thead>
                  <tr>
                   <th>header1</th>
                   <th>header2</th>
                  </tr>
                 </thead>
                 <tbody>
                  <tr>
                   <td>content</td>
                   <td>content</td>
                  </tr>
                 </tbody>
                </table>
            """.trimIndent()
        )
    }

    @Test
    fun `doesnt translate markdown table with extension property without Tables`() {
        val generatorContext = generatorContext().apply {
            workspace.views.configuration.addProperty("generatr.markdown.flexmark.extensions", "Admonition")
        }
        val viewModel = HomePageViewModel(generatorContext)

        val html = markdownToHtml(
            viewModel,
            """
                ## header

                | header1 | header2 |
                | ------- | ------- |
                | content | content |
            """.trimIndent(),
            svgFactory
        )

        assertThat(html).isEqualTo(
            """
                <h2>header</h2>
                <p>| header1 | header2 | | ------- | ------- | | content | content |</p>
            """.trimIndent()
        )
    }

    @Test
    fun `translates markdown table with extension property containing Tables`() {
        val generatorContext = generatorContext().apply {
            workspace.views.configuration.addProperty("generatr.markdown.flexmark.extensions", "Admonition, Tables")
        }
        val viewModel = HomePageViewModel(generatorContext)

        val html = markdownToHtml(
            viewModel,
            """
                ## header

                | header1 | header2 |
                | ------- | ------- |
                | content | content |
            """.trimIndent(),
            svgFactory
        )

        assertThat(html).isEqualTo(
            """
                <h2>header</h2>
                <table>
                 <thead>
                  <tr>
                   <th>header1</th>
                   <th>header2</th>
                  </tr>
                 </thead>
                 <tbody>
                  <tr>
                   <td>content</td>
                   <td>content</td>
                  </tr>
                 </tbody>
                </table>
            """.trimIndent()
        )
    }

    @Test
    fun `translates markdown admonition block with extension property containing Admonition`() {
        val generatorContext = generatorContext().apply {
            workspace.views.configuration.addProperty("generatr.markdown.flexmark.extensions", "Admonition, Tables")
        }
        val viewModel = HomePageViewModel(generatorContext)

        val html = markdownToHtml(
            viewModel,
            """
                ## header

                !!! faq "FAQ"
                    This is a FAQ.
            """.trimIndent(),
            svgFactory
        )

        assertThat(html).isEqualTo(
            """
                <svg xmlns="http://www.w3.org/2000/svg" class="adm-hidden">
                 <symbol id="adm-faq">
                  <svg viewbox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                   <path d="m20.4 12.2c0 4.5-3.7 8.2-8.2 8.2s-8.2-3.7-8.2-8.2 3.7-8.2 8.2-8.2 8.2 3.7 8.2 8.2zm-4.2-2.9c-.1-1.3-1-2.5-2.3-2.9-.8-.2-1.6-.1-2.4.1-.6.1-1.5.2-1.8.6-.4.5.2 1 .6 1.3.6.4 1.1-.1 1.8-.3s3-.7 2.4.8c-.5 1.1-2 1.9-3 2.6-.4.3-.9.6-1.2 1-.5.6.1 1 .7 1.4.4.3.9.2 1.2-.1.5-.5 1-.9 1.6-1.3 1.1-.7 2.5-1.6 2.4-3.2-.2-1.3 0 .7 0 0zm-2.8 6.8c-.3-.5-1.3-1.3-1.9-1.1-.4.1-.4.4-.5.8-.1.2-.4.6-.3.8.1.4 1.5 1.3 1.9 1 .1-.1.9-1.4.8-1.5 0-.1.1.1 0 0z" fill="currentColor" />
                  </svg>
                 </symbol>
                </svg>
                <h2>header</h2>
                <div class="adm-block adm-faq">
                 <div class="adm-heading">
                  <svg class="adm-icon">
                   <use xlink:href="#adm-faq" />
                  </svg><span>FAQ</span>
                 </div>
                 <div class="adm-body">
                  <p>This is a FAQ.</p>
                 </div>
                </div>
            """.trimIndent()
        )
    }

    @Test
    fun `translates mermaid graphings in markdown with extension property containing GitLab`() {
        val generatorContext = generatorContext().apply {
            workspace.views.configuration.addProperty("generatr.markdown.flexmark.extensions", "Admonition, GitLab, Tables")
        }
        val viewModel = HomePageViewModel(generatorContext)

        val html = markdownToHtml(
            viewModel,
            """
                ## Mermaid

                ```mermaid
                graph TD;
                A-->B;
                A-->C;
                B-->D;
                C-->D;
                ```
            """.trimIndent(),
            svgFactory
        )

        assertThat(html).isEqualTo(
            """
            <h2>Mermaid</h2>
            <pre><code class="language-mermaid">graph TD;
            A--&gt;B;
            A--&gt;C;
            B--&gt;D;
            C--&gt;D;
            </code></pre>
            """.trimIndent()
        )
    }

    @Test
    fun `translates mermaid graphings in markdown without extension property containing GitLab`() {
        val generatorContext = generatorContext().apply {
            workspace.views.configuration.addProperty("generatr.markdown.flexmark.extensions", "Admonition, Tables")
        }
        val viewModel = HomePageViewModel(generatorContext)

        val html = markdownToHtml(
            viewModel,
            """
                ## Mermaid

                ```mermaid
                graph TD;
                A-->B;
                A-->C;
                B-->D;
                C-->D;
                ```
            """.trimIndent(),
            svgFactory
        )

        assertThat(html).isEqualTo(
            """
            <h2>Mermaid</h2>
            <pre><code class="language-mermaid">graph TD;
            A--&gt;B;
            A--&gt;C;
            B--&gt;D;
            C--&gt;D;
            </code></pre>
            """.trimIndent()
        )
    }

    @Test
    fun `embedded diagram`() {
        val generatorContext = generatorContext()
        val viewModel = HomePageViewModel(generatorContext)

        val html = markdownToHtml(viewModel, "![System Landscape Diagram](embed:SystemLandscape)", svgFactory)

        val randomId = html.substringAfter("<div id=\"").substringBefore("<svg").substring(0, 15)


        assertThat(html).isEqualTo(
            """
                <p>
                 <div>
                  <figure style="width: min(100%, 800px);">
                   <div id="$randomId">
                    <svg viewbox="0 0 800 900"></svg>
                   </div>
                   <script type="text/javascript">
                                    var elm = document.getElementById("$randomId");
                                    elm.setAttribute("style","width: min(100%, 800px); height: 900px;");
                                    var svgElement = elm.firstElementChild;
                                    svgElement.setAttribute("style","display: inline; width: inherit; min-width: inherit; max-width: inherit; height: inherit; min-height: inherit; max-height: inherit; ");
                                    var panZoomBox_${randomId} = svgPanZoom(svgElement, {
                                        zoomEnabled: true,
                                        controlIconsEnabled: true,
                                        fit: true,
                                        center: true,
                                        minZoom: 1,
                                        maxZoom: 5
                                    });
                                    </script>
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
