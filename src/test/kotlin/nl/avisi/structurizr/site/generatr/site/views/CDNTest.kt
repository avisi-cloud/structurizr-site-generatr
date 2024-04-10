package nl.avisi.structurizr.site.generatr.site.views

import assertk.all
import assertk.assertThat
import assertk.assertions.doesNotContain
import assertk.assertions.endsWith
import assertk.assertions.startsWith
import com.structurizr.Workspace
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

class CDNTest {

    private val workspace = Workspace("workspace name", "")
    private val cdn = CDN(workspace)

    @TestFactory
    fun `cdn locations`() = listOf(
        cdn.bulmaCss() to "/css/bulma.min.css",
        cdn.katexJs() to "/dist/katex.min.js",
        cdn.katexCss() to "/dist/katex.min.css",
        cdn.lunrJs() to "/lunr.min.js",
        cdn.lunrLanguagesStemmerJs() to "/min/lunr.stemmer.support.min.js",
        cdn.lunrLanguagesJs("en") to "/min/lunr.en.min.js",
        cdn.mermaidJs() to "/dist/mermaid.esm.min.mjs",
        cdn.svgpanzoomJs() to "/dist/svg-pan-zoom.min.js",
        cdn.webfontloaderJs() to "/webfontloader.js"
    ).map { (url, suffix) ->
        DynamicTest.dynamicTest(url) {
            assertThat(url).all {
                startsWith("https://")
                endsWith(suffix)
                doesNotContain("~", "^", ">", "<", "=", ":=")
            }
        }
    }
}
