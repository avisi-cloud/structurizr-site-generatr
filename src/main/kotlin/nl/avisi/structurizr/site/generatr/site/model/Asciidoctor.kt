package nl.avisi.structurizr.site.generatr.site.model

import org.asciidoctor.Asciidoctor

val asciidoctor: Asciidoctor = Asciidoctor.Factory.create().apply {
    javaConverterRegistry().register(AsciiDocTextConverter::class.java)
}
