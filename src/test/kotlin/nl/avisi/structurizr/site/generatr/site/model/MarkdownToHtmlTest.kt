package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.matches
import com.structurizr.documentation.Format
import org.junit.jupiter.api.Test

class MarkdownToHtmlTest : ViewModelTest() {

    @Test
    fun `translates markdown`() {
        val generatorContext = generatorContext()
        val viewModel = HomePageViewModel(generatorContext)

        val html = toHtml(
            viewModel,
            """
                ## header
                content
            """.trimIndent(),
            Format.Markdown,
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

        val html = toHtml(
            viewModel,
            """
                ## header

                | header1 | header2 |
                | ------- | ------- |
                | content | content |
            """.trimIndent(),
            Format.Markdown,
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

        val html = toHtml(
            viewModel,
            """
                ## header

                | header1 | header2 |
                | ------- | ------- |
                | content | content |
            """.trimIndent(),
            Format.Markdown,
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

        val html = toHtml(
            viewModel,
            """
                ## header

                | header1 | header2 |
                | ------- | ------- |
                | content | content |
            """.trimIndent(),
            Format.Markdown,
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

        val html = toHtml(
            viewModel,
            """
                ## header

                !!! faq "FAQ"
                    This is a FAQ.
            """.trimIndent(),
            Format.Markdown,
            svgFactory
        )

        assertThat(html).isEqualTo(
            """
                <svg xmlns="http://www.w3.org/2000/svg" class="adm-hidden">
                 <symbol id="adm-faq">
                  <svg viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                   <path d="m20.4 12.2c0 4.5-3.7 8.2-8.2 8.2s-8.2-3.7-8.2-8.2 3.7-8.2 8.2-8.2 8.2 3.7 8.2 8.2zm-4.2-2.9c-.1-1.3-1-2.5-2.3-2.9-.8-.2-1.6-.1-2.4.1-.6.1-1.5.2-1.8.6-.4.5.2 1 .6 1.3.6.4 1.1-.1 1.8-.3s3-.7 2.4.8c-.5 1.1-2 1.9-3 2.6-.4.3-.9.6-1.2 1-.5.6.1 1 .7 1.4.4.3.9.2 1.2-.1.5-.5 1-.9 1.6-1.3 1.1-.7 2.5-1.6 2.4-3.2-.2-1.3 0 .7 0 0zm-2.8 6.8c-.3-.5-1.3-1.3-1.9-1.1-.4.1-.4.4-.5.8-.1.2-.4.6-.3.8.1.4 1.5 1.3 1.9 1 .1-.1.9-1.4.8-1.5 0-.1.1.1 0 0z" fill="currentColor" />
                  </svg>
                 </symbol>
                </svg>
                <h2>header</h2>
                <div class="adm-block adm-faq">
                 <div class="adm-heading">
                  <svg class="adm-icon">
                   <use xlink:href="#adm-faq" />
                  </svg>
                  <span>FAQ</span>
                 </div>
                 <div class="adm-body">
                  <p>This is a FAQ.</p>
                 </div>
                </div>
            """.trimIndent()
        )
    }

    @Test
    fun `renders PlantUML code to SVG`() {
        val generatorContext = generatorContext()
        val viewModel = HomePageViewModel(generatorContext)

        val html = toHtml(
            viewModel,
            """
                ```puml
                @startuml
                class Foo
                @enduml
                ```
            """.trimIndent(),
            Format.Markdown,
            svgFactory
        )

        assertThat(html).matches("<div>.*<svg.*Foo.*</svg>.*</div>".toRegex(RegexOption.DOT_MATCHES_ALL))
    }

    @Test
    fun `translates mermaid graphings in markdown with extension property containing GitLab`() {
        val generatorContext = generatorContext().apply {
            workspace.views.configuration.addProperty("generatr.markdown.flexmark.extensions", "Admonition, GitLab, Tables")
        }
        val viewModel = HomePageViewModel(generatorContext)

        val html = toHtml(
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
            Format.Markdown,
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

        val html = toHtml(
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
            Format.Markdown,
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

        val html = toHtml(viewModel, "![System Landscape Diagram](embed:SystemLandscape)", Format.Markdown, svgFactory)

        assertThat(html).isEqualTo(
            """
                <p>
                 <div>
                  <figure style="width: min(100%, 800px);" id="SystemLandscape">
                   <div>
                    <svg viewBox="0 0 800 900"></svg>
                   </div>
                   <div style="width: min(100%, 800px);">
                    <div>
                     <svg viewBox="0 0 800 900"></svg>
                    </div>
                   </div>
                   <figcaption>
                    <a onclick="openSvgModal('SystemLandscape-modal', 'SystemLandscape-svg')">System Landscape Diagram</a>
                   </figcaption>
                  </figure>
                  <div class="modal" id="SystemLandscape-modal">
                   <div class="modal-background" onclick="closeModal('SystemLandscape-modal')"></div>
                   <div class="modal-content">
                    <div class="box">
                     <div id="SystemLandscape-svg" class="modal-box-content">
                      <svg viewBox="0 0 800 900"></svg>
                     </div>
                     <div class="has-text-centered">
                      System Landscape Diagram [<a href="svg/SystemLandscape.svg" target="_blank">svg</a>|<a href="png/SystemLandscape.png" target="_blank">png</a>|<a href="puml/SystemLandscape.puml" target="_blank">puml</a>]
                      <br>
                      Legend [<a href="svg/SystemLandscape.legend.svg" target="_blank">svg</a>|<a href="png/SystemLandscape.legend.png" target="_blank">png</a>|<a href="svg/SystemLandscape.legend.svg" target="_blank">puml</a>]
                     </div>
                    </div>
                   </div>
                   <button class="modal-close is-large" aria-label="close" onclick="closeModal('SystemLandscape-modal')"></button>
                  </div>
                 </div>
                </p>
            """.trimIndent()
        )
    }

    @Test
    fun `embedded diagram key not found`() {
        val generatorContext = generatorContext()
        val viewModel = HomePageViewModel(generatorContext)

        val html = toHtml(viewModel, "![Diagram](embed:non-existing)", Format.Markdown) { _, _ ->
            null
        }

        assertThat(html).isEqualTo(
            """
                <p>
                 <div>
                  <div class="notification is-danger">
                   No view with key<span class="has-text-weight-bold"> non-existing </span>found!
                  </div>
                 </div>
                </p>
            """.trimIndent()
        )
    }

    @Test
    fun `links to pages`() {
        val generatorContext = generatorContext()
        val viewModel = HomePageViewModel(generatorContext)

        val html = toHtml(
            viewModel,
            """
                ## header
                Link: [Some decision](/decisions/1/),
            """.trimIndent(),
            Format.Markdown,
            svgFactory
        )

        assertThat(html).isEqualTo(
            """
                <h2>header</h2>
                <p>Link: <a href="decisions/1/">Some decision</a>,</p>
            """.trimIndent()
        )
    }

    @Test
    fun `links to files`() {
        val generatorContext = generatorContext()
        val viewModel = HomePageViewModel(generatorContext)

        val html = toHtml(
            viewModel,
            """
                ## header
                Link: [Some document](/document.md),
            """.trimIndent(),
            Format.Markdown,
            svgFactory
        )

        assertThat(html).isEqualTo(
            """
                <h2>header</h2>
                <p>Link: <a href="document.md">Some document</a>,</p>
            """.trimIndent()
        )
    }

    @Test
    fun `links to external sites`() {
        val generatorContext = generatorContext()
        val viewModel = HomePageViewModel(generatorContext)

        val html = toHtml(
            viewModel,
            """
                ## header
                Link: [Wikipedia](https://www.wikipedia.org/),
            """.trimIndent(),
            Format.Markdown,
            svgFactory
        )

        assertThat(html).isEqualTo(
            """
                <h2>header</h2>
                <p>Link: <a href="https://www.wikipedia.org/" target="blank">Wikipedia</a>,</p>
            """.trimIndent()
        )
    }
}
