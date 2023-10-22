package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.documentation.Decision
import com.structurizr.documentation.Format
import com.structurizr.documentation.Section
import com.vladsch.flexmark.ast.Heading
import com.vladsch.flexmark.ast.Paragraph
import com.vladsch.flexmark.parser.Parser
import org.asciidoctor.Asciidoctor
import org.asciidoctor.Options
import org.asciidoctor.SafeMode
import org.asciidoctor.ast.ContentNode
import org.asciidoctor.ast.Document
import org.asciidoctor.ast.StructuralNode
import org.asciidoctor.converter.ConverterFor
import org.asciidoctor.converter.StringConverter

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
    val asciidoctor = Asciidoctor.Factory.create()
    asciidoctor.javaConverterRegistry().register(AsciiDocTextConverter::class.java)

    val options = Options.builder().safe(SafeMode.SERVER).backend("text").build()
    val text = asciidoctor.convert(content, options)
    asciidoctor.shutdown()

    return text.lines().joinToString(" ")
}

@ConverterFor("text")
class AsciiDocTextConverter(
    backend: String?,
    opts: Map<String?, Any?>?
) : StringConverter(backend, opts) {
    override fun convert(node: ContentNode, transform: String?, o: Map<Any?, Any?>?): String? {
        val transform1 = transform ?: node.nodeName
        return if (node is Document)
            node.content.toString()
        else if (node is org.asciidoctor.ast.Section)
            "${node.title}\n${node.content}"
        else if (transform1 == "preamble" || transform1 == "paragraph")
            (node as StructuralNode).content as String
        else
            null
    }
}
