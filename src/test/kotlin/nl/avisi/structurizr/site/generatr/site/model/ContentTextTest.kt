package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.structurizr.documentation.Format
import com.structurizr.documentation.Section
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class ContentTextTest {

    @Test
    fun `no content`() {
        val section = Section(Format.Markdown, "")
        assertThat(section.contentText()).isEqualTo("")
    }

    @Test
    fun `only whitespaces`() {
        val section = Section(Format.Markdown, " \n ")
        assertThat(section.contentText()).isEqualTo("")
    }

    @Test
    fun `no markdown`() {
        val section = Section(Format.AsciiDoc, "== title")
        assertThat(section.contentText()).isEqualTo("")
    }

    @ParameterizedTest
    @ValueSource(strings = ["# header", "## header", "### header"])
    fun `ignores title`(content: String) {
        val section = Section(Format.Markdown, content)
        assertThat(section.contentText()).isEqualTo("")
    }

    @Test
    fun `simple paragraph`() {
        val section = Section(Format.Markdown, "some content")
        assertThat(section.contentText()).isEqualTo("some content")
    }

    @Test
    fun `headers and paragraphs`() {
        val section = Section(Format.Markdown, "# header\nsome content\n## subheader\nmore content")
        assertThat(section.contentText()).isEqualTo("subheader some content more content")
    }
}
