package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.structurizr.documentation.Format
import com.structurizr.documentation.Section
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class SectionTitleTest {

    @ParameterizedTest
    @ValueSource(strings = ["Markdown", "AsciiDoc"])
    fun `no content`(format: Format) {
        val section = Section(format, "")
        assertThat(section.title()).isEqualTo("untitled document")
    }

    @ParameterizedTest
    @ValueSource(strings = ["Markdown", "AsciiDoc"])
    fun `only whitespaces`(format: Format) {
        val section = Section(format, " \n ")
        assertThat(section.title()).isEqualTo("untitled document")
    }

    @ParameterizedTest
    @ValueSource(strings = ["Markdown", "AsciiDoc"])
    fun `invalid format`(format: Format) {
        val section = Section(format, "<h1>title</h1>")
        assertThat(section.title()).isEqualTo("unknown document")
    }

    @ParameterizedTest
    @ValueSource(strings = ["Markdown", "AsciiDoc"])
    fun `short paragraph`(format: Format) {
        val section = Section(format, "some content")
        assertThat(section.title()).isEqualTo("some content")
    }

    @ParameterizedTest
    @ValueSource(strings = ["Markdown", "AsciiDoc"])
    fun `long paragraph`(format: Format) {
        val section = Section(
            format,
            "some very very long content we really need to truncate since no one wants to read such an exhausting title"
        )
        assertThat(section.title()).isEqualTo("some very very long content we really need to")
    }

    @ParameterizedTest
    @ValueSource(strings = ["Markdown", "AsciiDoc"])
    fun `long paragraph without whitespaces`(format: Format) {
        val section = Section(
            format,
            "some-very-very-long-content-we-really-need-to-truncate-since-no-one-wants-to-read-such-an-exhausting-title"
        )
        assertThat(section.title()).isEqualTo("some-very-very-long-content-we-really-need-to-trun")
    }

    @ParameterizedTest
    @ValueSource(strings = ["# header", "## header", "### header"])
    fun `with heading`(content: String) {
        val section = Section(Format.Markdown, content)
        assertThat(section.title()).isEqualTo("header")
    }
}
