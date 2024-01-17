package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.documentation.Decision
import com.structurizr.documentation.Format
import com.structurizr.documentation.Section
import com.vladsch.flexmark.ast.Heading
import com.vladsch.flexmark.ast.Paragraph
import com.vladsch.flexmark.parser.Parser
import org.asciidoctor.Options
import org.asciidoctor.SafeMode

private val parser = Parser.builder().build()

fun Decision.contentText(): String = when (format) {
    Format.Markdown -> markdownText(content)
    Format.AsciiDoc -> asciidocText(content)
    else -> ""
}

fun Section.contentText() = when (format) {
    Format.Markdown -> markdownText(content)
    Format.AsciiDoc -> asciidocText(content)
    else -> ""
}

private fun markdownText(content: String): String {
    val document = parser.parse(content)
    if (!document.hasChildren())
        return ""

    return document
        .children
        .filterIsInstance<Heading>()
        .drop(1) // ignore title
        .joinToString(" ") { it.text.toString() }
        .plus(" ")
        .plus(
            document
                .children
                .filterIsInstance<Paragraph>()
                .joinToString(" ") { it.chars.toString().trim() }
        )
        .trim()
}

private fun asciidocText(content: String): String {
    val options = Options.builder().safe(SafeMode.SERVER).backend("text").build()
    val text = asciidoctorWithTextConverter.convert(content, options)

    return text.lines().joinToString(" ")
}
