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
import kotlinx.html.unsafe
import nl.avisi.structurizr.site.generatr.site.asUrlRelativeTo
import nl.avisi.structurizr.site.generatr.site.model.MarkdownViewModel
import nl.avisi.structurizr.site.generatr.site.model.PageViewModel

fun FlowContent.markdown(pageViewModel: PageViewModel, markdown: MarkdownViewModel) {
    div {
        unsafe {
            +markdownToHtml(pageViewModel, markdown)
        }
    }
}

private fun markdownToHtml(pageViewModel: PageViewModel, markdown: MarkdownViewModel): String {
    val options = MutableDataSet()

    options.set(Parser.EXTENSIONS, listOf(TablesExtension.create()))

    val parser = Parser.builder(options).build()
    val renderer = HtmlRenderer.builder(options)
        .linkResolverFactory(CustomLinkResolver.Factory(pageViewModel))
        .build()
    val document = parser.parse(markdown.markdown)

    return renderer.render(document)
}

private class CustomLinkResolver(private val pageViewModel: PageViewModel) : LinkResolver {
    override fun resolveLink(node: Node, context: LinkResolverBasicContext, link: ResolvedLink): ResolvedLink {
        if (link.url.startsWith("embed:")) {
            val diagramId = link.url.substring(6)
            return link
                .withStatus(LinkStatus.VALID)
                .withUrl("/svg/$diagramId.svg".asUrlRelativeTo(pageViewModel.url))
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
