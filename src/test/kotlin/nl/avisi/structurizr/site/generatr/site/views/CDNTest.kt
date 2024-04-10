package nl.avisi.structurizr.site.generatr.site.views

import assertk.all
import assertk.assertThat
import assertk.assertions.*
import com.structurizr.Workspace
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

class CDNTest {
    @TestFactory
    fun `cdn locations`() {
        val workspace = Workspace("workspace name", "")
        val cdn = CDN(workspace)

        listOf(
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

    @Test
    fun `usage of default cdn url`() {
        val workspace = Workspace("workspace", "")
        val cdn = CDN(workspace)

        assertThat(cdn.workspace.views.configuration.properties["generatr.site.cdn"]).isNull()
        assertThat(cdn.getCdnBaseUrl()).isEqualTo("https://cdn.jsdelivr.net/npm")
    }

    @Test
    fun `usage of view configuration with ending slash`() {
        val workspace = Workspace("workspace name", "").apply {
            views.configuration.addProperty("generatr.site.cdn", "https://cdn.companyname.net/npm/")
        }
        val cdn = CDN(workspace)

        assertThat(cdn.getCdnBaseUrl()).isEqualTo("https://cdn.companyname.net/npm")
    }

    @Test
    fun `usage of view configuration without ending slash`() {
        val workspace = Workspace("workspace name", "").apply {
            views.configuration.addProperty("generatr.site.cdn", "https://cdn.companyname.net/npm")
        }
        val cdn = CDN(workspace)

        assertThat(cdn.getCdnBaseUrl()).isEqualTo("https://cdn.companyname.net/npm")
    }
}
