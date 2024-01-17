package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.documentation.Format
import com.structurizr.documentation.Section
import com.vladsch.flexmark.ast.Heading
import com.vladsch.flexmark.parser.Parser
import org.asciidoctor.Options
import org.asciidoctor.SafeMode

private val parser = Parser.builder().build()

fun Section.contentTitle(): String = when (format) {
    Format.Markdown -> markdownTitle()
    Format.AsciiDoc -> asciidocTitle()
    else -> "unsupported document"
}

private fun Section.markdownTitle(): String {
    val document = parser.parse(content)

    val header = document.children.firstOrNull { it is Heading }?.let { it as Heading }
    if (header != null)
        return header.text.toString()

    return "untitled document"
}

private fun Section.asciidocTitle(): String {
    val options = Options.builder().safe(SafeMode.SERVER).build()
    val document = asciidoctorWithTextConverter.load(content, options)

    if (document.title != null && document.title.isNotEmpty())
        return document.title

    return "untitled document"
}
