package nl.avisi.structurizr.site.generatr.site.views

import assertk.all
import assertk.assertThat
import assertk.assertions.doesNotContain
import assertk.assertions.endsWith
import assertk.assertions.startsWith
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

class CDNTest {
    @TestFactory
    fun `cdn locations`() = listOf(
        CDN.bulmaCss() to "/css/bulma.min.css",
        CDN.katexJs() to "/dist/katex.min.js",
        CDN.katexCss() to "/dist/katex.min.css",
        CDN.lunrJs() to "/lunr.min.js",
        CDN.lunrLanguagesStemmerJs() to "/min/lunr.stemmer.support.min.js",
        CDN.lunrLanguagesJs("en") to "/min/lunr.en.min.js",
        CDN.mermaidJs() to "/dist/mermaid.esm.min.mjs",
        CDN.webfontloaderJs() to "/webfontloader.js"
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
