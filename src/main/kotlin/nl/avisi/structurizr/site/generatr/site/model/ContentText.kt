package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.documentation.Decision
import com.structurizr.documentation.Format
import com.structurizr.documentation.Section
import com.vladsch.flexmark.ast.Heading
import com.vladsch.flexmark.ast.Paragraph
import com.vladsch.flexmark.parser.Parser

private val parser = Parser.builder().build()

fun Decision.contentText(): String {
    if (format != Format.Markdown)
        return ""

    return extractText(content)
}

fun Section.contentText(): String {
    if (format != Format.Markdown)
        return ""

    return extractText(content)
}

private fun extractText(content: String): String {
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
