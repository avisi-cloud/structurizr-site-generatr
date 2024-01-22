package nl.avisi.structurizr.site.generatr.site.model

import net.sourceforge.plantuml.FileFormat
import net.sourceforge.plantuml.FileFormatOption
import net.sourceforge.plantuml.SourceStringReader
import org.asciidoctor.Asciidoctor
import org.asciidoctor.ast.ContentModel
import org.asciidoctor.ast.ContentNode
import org.asciidoctor.ast.Document
import org.asciidoctor.ast.StructuralNode
import org.asciidoctor.converter.ConverterFor
import org.asciidoctor.converter.StringConverter
import org.asciidoctor.extension.BlockProcessor
import org.asciidoctor.extension.Contexts
import org.asciidoctor.extension.Name
import org.asciidoctor.extension.Reader
import java.io.ByteArrayOutputStream

val asciidoctorWithTextConverter: Asciidoctor = Asciidoctor.Factory.create().apply {
    javaConverterRegistry().register(AsciiDocTextConverter::class.java)
}

val asciidoctorWithPUMLRenderer: Asciidoctor = Asciidoctor.Factory.create().apply {
    javaExtensionRegistry().block(PlantUMLBlockProcessor::class.java)
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

@Name("plantuml")
@Contexts(value = [Contexts.LISTING])
@ContentModel(ContentModel.VERBATIM)
class PlantUMLBlockProcessor : BlockProcessor() {
    override fun process(parent: StructuralNode?, reader: Reader, attributes: MutableMap<String, Any>?): Any {
        val plantUMLCode = reader.read()
        val plantUMLReader = SourceStringReader(plantUMLCode)
        val stream = ByteArrayOutputStream()
        plantUMLReader.outputImage(stream, FileFormatOption(FileFormat.SVG, false))
        return createBlock(parent, "pass", "<div>$stream</div>")
    }
}
