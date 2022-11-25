package nl.avisi.structurizr.site.generatr.site.views

import com.vladsch.flexmark.ext.tables.TablesExtension
import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.html.LinkResolver
import com.vladsch.flexmark.html.LinkResolverFactory
import com.vladsch.flexmark.html.renderer.LinkResolverBasicContext
import com.vladsch.flexmark.html.renderer.LinkStatus
import com.vladsch.flexmark.html.renderer.ResolvedLink
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.util.ast.Node
import com.vladsch.flexmark.util.data.MutableDataSet
import kotlinx.html.FlowContent
import kotlinx.html.div
import kotlinx.html.stream.createHTML
import kotlinx.html.unsafe
import nl.avisi.structurizr.site.generatr.site.asUrlRelativeTo
import nl.avisi.structurizr.site.generatr.site.model.DiagramViewModel
import nl.avisi.structurizr.site.generatr.site.model.MarkdownViewModel
import nl.avisi.structurizr.site.generatr.site.model.PageViewModel
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

fun FlowContent.markdown(pageViewModel: PageViewModel, markdownViewModel: MarkdownViewModel) {
    div {
        unsafe {
            +markdownToHtml(pageViewModel, markdownViewModel)
        }
    }
}

private fun markdownToHtml(pageViewModel: PageViewModel, markdownViewModel: MarkdownViewModel): String {
    val options = MutableDataSet()

    options.set(Parser.EXTENSIONS, listOf(TablesExtension.create()))

    val parser = Parser.builder(options).build()
    val renderer = HtmlRenderer.builder(options)
        .linkResolverFactory(CustomLinkResolver.Factory(pageViewModel))
        .build()
    val markDownDocument = parser.parse(markdownViewModel.markdown)
    val html = renderer.render(markDownDocument)

    return Jsoup.parse(html)
        .apply { body().transformEmbeddedDiagramElements(pageViewModel, markdownViewModel.svgFactory) }
        .html()
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
            .withUrl("/${link.url.dropWhile { it == '/' }}".asUrlRelativeTo(pageViewModel.url))
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

private fun Element.transformEmbeddedDiagramElements(
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

private const val embedPrefix = "embed:"
