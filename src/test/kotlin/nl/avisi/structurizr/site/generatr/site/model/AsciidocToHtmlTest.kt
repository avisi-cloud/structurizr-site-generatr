package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.matches
import com.structurizr.documentation.Format
import org.junit.jupiter.api.Test

class AsciidocToHtmlTest : ViewModelTest() {

    @Test
    fun `translates asciidoc`() {
        val generatorContext = generatorContext()
        val viewModel = HomePageViewModel(generatorContext)

        val html = toHtml(
            viewModel,
            """
            == header
            content
            """.trimIndent(),
            Format.AsciiDoc,
            svgFactory
        )

        assertThat(html).isEqualTo(
            """
            <div class="sect1 content">
             <h2 id="_header">header</h2>
             <div class="sectionbody">
              <div class="paragraph content">
               <p>content</p>
              </div>
             </div>
            </div>
            """.trimIndent()
        )
    }

    @Test
    fun `translates asciidoc table`() {
        val generatorContext = generatorContext()
        val viewModel = HomePageViewModel(generatorContext)

        val html = toHtml(
            viewModel,
            """
            == header

            |===
            |header1 |header2

            |content
            |content
            |===
            """.trimIndent(),
            Format.AsciiDoc,
            svgFactory
        )

        assertThat(html).isEqualTo(
            """
            <div class="sect1 content">
             <h2 id="_header">header</h2>
             <div class="sectionbody">
              <table class="tableblock frame-all grid-all stretch content">
               <colgroup>
                <col style="width: 50%;">
                <col style="width: 50%;">
               </colgroup>
               <thead>
                <tr>
                 <th class="tableblock halign-left valign-top content">header1</th>
                 <th class="tableblock halign-left valign-top content">header2</th>
                </tr>
               </thead>
               <tbody>
                <tr>
                 <td class="tableblock halign-left valign-top content">
                  <p class="tableblock content">content</p>
                 </td>
                 <td class="tableblock halign-left valign-top content">
                  <p class="tableblock content">content</p>
                 </td>
                </tr>
               </tbody>
              </table>
             </div>
            </div>
            """.trimIndent()
        )
    }

    @Test
    fun `translates asciidoc admonition block`() {
        val generatorContext = generatorContext()
        val viewModel = HomePageViewModel(generatorContext)

        val html = toHtml(
            viewModel,
            """
            == header

            NOTE: This is a note.
            """.trimIndent(),
            Format.AsciiDoc,
            svgFactory
        )

        assertThat(html).isEqualTo(
            """
            <div class="sect1 content">
             <h2 id="_header">header</h2>
             <div class="sectionbody">
              <div class="admonitionblock note content adm-block adm-note">
               <table>
                <tbody>
                 <tr>
                  <td class="icon adm-heading">
                   <div class="title">Note</div>
                  </td>
                  <td class="content">This is a note.</td>
                 </tr>
                </tbody>
               </table>
              </div>
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
                [plantuml]
                ----
                @startuml
                class Foo
                @enduml
                ----
            """.trimIndent(),
            Format.AsciiDoc,
            svgFactory
        )

        assertThat(html).matches("<div>.*<svg.*Foo.*</svg>.*</div>".toRegex(RegexOption.DOT_MATCHES_ALL))
    }

    @Test
    fun `translates mermaid graphings in asciidoc`() {
        val generatorContext = generatorContext()
        val viewModel = HomePageViewModel(generatorContext)

        val html = toHtml(
            viewModel,
            """
            == Mermaid

            [source, mermaid]
            ----
            graph TD;
            A-->B;
            A-->C;
            B-->D;
            C-->D;
            ----
            """.trimIndent(),
            Format.AsciiDoc,
            svgFactory
        )

        assertThat(html).isEqualTo(
            """
            <div class="sect1 content">
             <h2 id="_mermaid">Mermaid</h2>
             <div class="sectionbody">
              <div class="listingblock content">
               <div class="content">
                <pre class="highlight"><code class="language-mermaid" data-lang="mermaid">graph TD;
            A--&gt;B;
            A--&gt;C;
            B--&gt;D;
            C--&gt;D;</code></pre>
               </div>
              </div>
             </div>
            </div>
            """.trimIndent()
        )
    }

    @Test
    fun `embedded diagram`() {
        val generatorContext = generatorContext()
        val viewModel = HomePageViewModel(generatorContext)

        val html = toHtml(viewModel, "image::embed:SystemLandscape[System Landscape Diagram]", Format.AsciiDoc, svgFactory)

        assertThat(html).isEqualTo(
            """
            <div class="imageblock content">
             <div class="content">
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
             </div>
            </div>
            """.trimIndent()
        )
    }

    @Test
    fun `embedded diagram key not found`() {
        val generatorContext = generatorContext()
        val viewModel = HomePageViewModel(generatorContext)

        val html = toHtml(viewModel, "image::embed:non-existing[Diagram]", Format.AsciiDoc) { _, _ ->
            null
        }

        assertThat(html).isEqualTo(
            """
            <div class="imageblock content">
             <div class="content">
              <div>
               <div class="notification is-danger">
                No view with key<span class="has-text-weight-bold"> non-existing </span>found!
               </div>
              </div>
             </div>
            </div>
            """.trimIndent()
        )
    }
}
