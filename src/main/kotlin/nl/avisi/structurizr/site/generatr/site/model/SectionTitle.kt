package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.documentation.Section
import com.vladsch.flexmark.ast.Heading
import com.vladsch.flexmark.ast.Paragraph
import com.vladsch.flexmark.parser.Parser

private val parser = Parser.builder().build()
private const val MAX_TITLE_LENGTH = 50

fun Section.title(): String {
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
