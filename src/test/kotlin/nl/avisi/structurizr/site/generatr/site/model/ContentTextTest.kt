package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.structurizr.documentation.Format
import com.structurizr.documentation.Section
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class ContentTextTest {

    @TestFactory
    fun `no content`() = listOf(Format.Markdown, Format.AsciiDoc)
        .map { format ->
            DynamicTest.dynamicTest(format.name) {
                val section = Section(format, "")
                assertThat(section.contentText()).isEqualTo("")
            }
        }

    @TestFactory
    fun `only whitespaces`() = listOf(Format.Markdown, Format.AsciiDoc)
        .map { format ->
            DynamicTest.dynamicTest(format.name) {
                val section = Section(format, " \n ")
                assertThat(section.contentText()).isEqualTo("")
            }
        }

    @ParameterizedTest
    @ValueSource(strings = ["# header", "## header", "### header"])
    fun `ignores markdown title`(content: String) {
        val section = Section(Format.Markdown, content)
        assertThat(section.contentText()).isEqualTo("")
    }

    @ParameterizedTest
    @ValueSource(strings = ["= header", "== header", "=== header"])
    fun `ignores asciidoc title`(content: String) {
        val section = Section(Format.AsciiDoc, content)
        assertThat(section.contentText()).isEqualTo("")
    }

    @TestFactory
    fun `simple paragraph`() = listOf(Format.Markdown, Format.AsciiDoc)
        .map { format ->
            DynamicTest.dynamicTest(format.name) {
                val section = Section(format, "some content")
                assertThat(section.contentText()).isEqualTo("some content")
            }
        }

    @Test
    fun `markdown headers and paragraphs`() {
        val section = Section(Format.Markdown, "# header\nsome content\n## subheader\nmore content")
        assertThat(section.contentText()).isEqualTo("subheader some content more content")
    }

    @Test
    fun `asciidoc headers and paragraphs`() {
        val section = Section(Format.AsciiDoc, "= header\nsome content\n== subheader\nmore content")
        assertThat(section.contentText()).isEqualTo("subheader some content more content")
    }
}
