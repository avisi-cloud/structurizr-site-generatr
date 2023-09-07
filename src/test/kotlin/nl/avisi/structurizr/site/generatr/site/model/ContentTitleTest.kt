package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.structurizr.documentation.Format
import com.structurizr.documentation.Section
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class ContentTitleTest {

    @TestFactory
    fun `no content`() = listOf(Format.Markdown, Format.AsciiDoc)
        .map { format ->
            DynamicTest.dynamicTest(format.name) {
                val section = Section(format, "")
                assertThat(section.contentTitle()).isEqualTo("untitled document")
            }
        }

    @TestFactory
    fun `only whitespaces`() = listOf(Format.Markdown, Format.AsciiDoc)
        .map { format ->
            DynamicTest.dynamicTest(format.name) {
                val section = Section(format, " \n ")
                assertThat(section.contentTitle()).isEqualTo("untitled document")
            }
        }

    @TestFactory
    fun `simple paragraph`() = listOf(Format.Markdown, Format.AsciiDoc)
        .map { format ->
            DynamicTest.dynamicTest(format.name) {
                val section = Section(format, "some content")
                assertThat(section.contentTitle()).isEqualTo("some content")
            }
        }

    @TestFactory
    fun `long paragraph`() = listOf(Format.Markdown, Format.AsciiDoc)
        .map { format ->
            DynamicTest.dynamicTest(format.name) {
                val section = Section(
                    format,
                    "some very very long content we really need to truncate since no one wants to read such an exhausting title"
                )
                assertThat(section.contentTitle()).isEqualTo("some very very long content we really need to")
            }
        }

    @TestFactory
    fun `long paragraph without whitespaces`() = listOf(Format.Markdown, Format.AsciiDoc)
        .map { format ->
            DynamicTest.dynamicTest(format.name) {
                val section = Section(
                    format,
                    "some-very-very-long-content-we-really-need-to-truncate-since-no-one-wants-to-read-such-an-exhausting-title"
                )
                assertThat(section.contentTitle()).isEqualTo("some-very-very-long-content-we-really-need-to-trun")
            }
        }

    @ParameterizedTest
    @ValueSource(strings = ["# header", "## header", "### header"])
    fun `with markdown heading`(content: String) {
        val section = Section(Format.Markdown, content)
        assertThat(section.contentTitle()).isEqualTo("header")
    }

    @ParameterizedTest
    @ValueSource(strings = ["= header", "== header", "=== header"])
    fun `with asciidoc heading`(content: String) {
        val section = Section(Format.AsciiDoc, content)
        assertThat(section.contentTitle()).isEqualTo("header")
    }
}
