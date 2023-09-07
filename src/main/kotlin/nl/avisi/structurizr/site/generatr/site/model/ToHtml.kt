package nl.avisi.structurizr.site.generatr.site.model

import com.structurizr.documentation.Format
import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.html.LinkResolver
import com.vladsch.flexmark.html.LinkResolverFactory
import com.vladsch.flexmark.html.renderer.LinkResolverBasicContext
import com.vladsch.flexmark.html.renderer.LinkStatus
import com.vladsch.flexmark.html.renderer.ResolvedLink
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.util.ast.Node
import kotlinx.html.div
import kotlinx.html.stream.createHTML
import nl.avisi.structurizr.site.generatr.site.asUrlToFile
import nl.avisi.structurizr.site.generatr.site.views.diagram
import org.asciidoctor.Asciidoctor
import org.asciidoctor.Options
import org.asciidoctor.SafeMode
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

const val embedPrefix = "embed:"

fun toHtml(
    pageViewModel: PageViewModel,
    content: String,
    format: Format,
    svgFactory: (key: String, url: String) -> String?
): String = when (format) {
    Format.Markdown -> markdownToHtml(pageViewModel, content, svgFactory)
    Format.AsciiDoc -> asciidocToHtml(pageViewModel, content, svgFactory)
    else -> "unsupported document"
}

private fun markdownToHtml(
    pageViewModel: PageViewModel,
    markdown: String,
    svgFactory: (key: String, url: String) -> String?
): String {
    val flexmarkConfig = pageViewModel.flexmarkConfig
    val options = flexmarkConfig.flexmarkOptions

    val parser = Parser.builder(options).build()
    val renderer = HtmlRenderer.builder(options)
        .linkResolverFactory(CustomLinkResolver.Factory(pageViewModel))
        .build()
    val markDownDocument = parser.parse(markdown)
    val html = renderer.render(markDownDocument)

    return Jsoup.parse(html)
        .apply { body().transformEmbeddedDiagramElements(pageViewModel, svgFactory) }
        .body()
        .html()
}

private fun asciidocToHtml(
    pageViewModel: PageViewModel,
    asciidoc: String,
    svgFactory: (key: String, url: String) -> String?
): String {
    val asciidoctor = Asciidoctor.Factory.create()
    val options = Options.builder()
        .safe(SafeMode.SERVER)
        // Docs dir needs to be exposed from structurizr, which is not the case at the moment.
        // Needed for partial include `include::partial.adoc[]`, which structurizr also does not support.
        // see https://docs.asciidoctor.org/asciidoc/latest/directives/include/
        // another option could be https://docs.asciidoctor.org/asciidoctorj/latest/locating-files/#globdirectorywalker-class
        // .baseDir(File("./docs/example/workspace-docs"))
        .backend("html5")
        .build()
    val html = asciidoctor.convert(asciidoc, options)
    asciidoctor.shutdown()
    return Jsoup.parse(html)
        .apply {
            body().transformEmbeddedDiagramElements(pageViewModel, svgFactory)
            body().transformAsciiDocContent()
            body().transformAsciiDocAdmonition()
            body().transformAsciiDocImgSrc(pageViewModel)
        }
        .body()
        .html()
}

/**
 * Adds css class content to several asciidoc tags, as the default asciidoc structure is not compatible with bulma css.
 * [asciidoctor html5 template](https://github.com/asciidoctor/asciidoctor/blob/main/lib/asciidoctor/converter/html5.rb)
 */
private fun Element.transformAsciiDocContent() = this
    .select(".sect0, .sect1, .sect2, .sect3, .sect4, .sect5, .paragraph, .toc, .olist, .ulist, .dlist, .colist, .listingblock, .tableblock, .literalblock, .quoteblock, .stemblock, .verseblock, .imageblock, .videoblock, .audioblock, .admonitionblock")
    .addClass("content")

private fun Element.transformAsciiDocAdmonition() = this
    .select(".admonitionblock").addClass("adm-block")
    .select("td.icon").addClass("adm-heading").parents()
    .select(".admonitionblock.note").addClass("adm-note").parents()
    .select(".admonitionblock.tip").addClass("adm-tip").parents()
    .select(".admonitionblock.important").addClass("adm-example").parents()
    .select(".admonitionblock.caution").addClass("adm-danger").parents()
    .select(".admonitionblock.warning").addClass("adm-warning")

private fun Element.transformAsciiDocImgSrc(pageViewModel: PageViewModel) = this
    .select("img").map {
        it.attr("src", "/${it.attr("src").dropWhile { it == '/' }}".asUrlToFile(pageViewModel.url))
    }

private class CustomLinkResolver(private val pageViewModel: PageViewModel) : LinkResolver {
    override fun resolveLink(node: Node, context: LinkResolverBasicContext, link: ResolvedLink): ResolvedLink {
        if (link.url.startsWith(embedPrefix)) {
            return link
                .withStatus(LinkStatus.VALID)
                .withUrl(link.url)
        }
        if (link.url.matches("https?://.*".toRegex()))
            return link

        return link.withStatus(LinkStatus.VALID)
            .withUrl("/${link.url.dropWhile { it == '/' }}".asUrlToFile(pageViewModel.url))
    }

    class Factory(private val viewModel: PageViewModel) : LinkResolverFactory {
        override fun apply(context: LinkResolverBasicContext): LinkResolver {
            return CustomLinkResolver(viewModel)
        }

        override fun getAfterDependents(): MutableSet<Class<*>>? = null

        override fun getBeforeDependents(): MutableSet<Class<*>>? = null

        override fun affectsGlobalScope() = false
    }
}

fun Element.transformEmbeddedDiagramElements(
    pageViewModel: PageViewModel,
    svgFactory: (key: String, url: String) -> String?
) = this.allElements
    .toList()
    .filter { it.tag().name == "img" && it.attr("src").startsWith(embedPrefix) }
    .forEach {
        val key = it.attr("src").substring(embedPrefix.length)
        val name = it.attr("alt").ifBlank { key }
        val html = createHTML().div {
            diagram(DiagramViewModel.forView(pageViewModel, key, name, svgFactory))
        }

        it.parent()?.append(html)
        it.remove()
    }
