package nl.avisi.structurizr.site.generatr.site.model

import org.asciidoctor.Asciidoctor
import org.asciidoctor.ast.ContentNode
import org.asciidoctor.ast.Document
import org.asciidoctor.ast.StructuralNode
import org.asciidoctor.converter.ConverterFor
import org.asciidoctor.converter.StringConverter

val asciidoctor: Asciidoctor = Asciidoctor.Factory.create().apply {
    javaConverterRegistry().register(AsciiDocTextConverter::class.java)
}

@ConverterFor("text")
class AsciiDocTextConverter(
    backend: String?,
    opts: Map<String?, Any?>?
) : StringConverter(backend, opts) {
    // based on https://docs.asciidoctor.org/asciidoctorj/latest/write-converter/

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
