package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.documentation.Format
import com.structurizr.documentation.Section
import com.vladsch.flexmark.ast.Heading
import com.vladsch.flexmark.ast.Paragraph
import com.vladsch.flexmark.parser.Parser
import org.asciidoctor.Asciidoctor
import org.asciidoctor.Options
import org.asciidoctor.SafeMode

private val parser = Parser.builder().build()
private const val MAX_TITLE_LENGTH = 50

fun Section.contentTitle(): String = when (format) {
    Format.Markdown -> markdownTitle()
    Format.AsciiDoc -> asciidocTitle()
    else -> "unsupported document"
}

private fun Section.markdownTitle(): String {
    val document = parser.parse(content)

    if (!document.hasChildren())
        return "untitled document"

    val header = document.children.firstOrNull { it is Heading }?.let { it as Heading }
    if (header != null)
        return header.text.toString()

    val paragraph = document.children.firstOrNull { it is Paragraph }?.let { it as Paragraph }?.chars?.toString()
    if (paragraph != null)
        return if (paragraph.length > MAX_TITLE_LENGTH) {
            val whitespacePosition = paragraph.withIndex()
                .filter { it.value.isWhitespace() }
                .lastOrNull { it.index < MAX_TITLE_LENGTH }
                ?.index
            paragraph.take(whitespacePosition ?: MAX_TITLE_LENGTH)
        } else paragraph

    return "unknown document"
}

private fun Section.asciidocTitle(): String {
    val asciidoctor = Asciidoctor.Factory.create()
    val options = Options.builder().safe(SafeMode.SERVER).build()
    val document = asciidoctor.load(content, options)
    asciidoctor.shutdown()
    if (document.title != null && document.title.isNotEmpty())
        return document.title

    // TODO Content extraction is not implemented yet for AsciiDoc

    return "untitled document"
}
