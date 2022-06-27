package nl.avisi.structurizr.site.generatr.site.components

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
import kotlinx.html.DIV
import kotlinx.html.unsafe
import nl.avisi.structurizr.site.generatr.site.context.AbstractPageContext
import nl.avisi.structurizr.site.generatr.site.makeUrlRelative

fun DIV.renderedMarkdown(context: AbstractPageContext, markdown: String) {
    unsafe {
        +renderMarkdownToHtml(context, markdown)
    }
}

private fun renderMarkdownToHtml(context: AbstractPageContext, markdown: String): String {
    val options = MutableDataSet()

    options.set(Parser.EXTENSIONS, listOf(TablesExtension.create()))

    val parser = Parser.builder(options).build()
    val renderer = HtmlRenderer.builder(options)
        .linkResolverFactory(CustomLinkResolver.Factory(context))
        .build()
    val document = parser.parse(markdown)

    return renderer.render(document)
}

private class CustomLinkResolver(private val pageContext: AbstractPageContext) : LinkResolver {
    override fun resolveLink(node: Node, context: LinkResolverBasicContext, link: ResolvedLink): ResolvedLink {
        if (link.url.startsWith("embed:")) {
            return link
                .withStatus(LinkStatus.VALID)
                .withUrl(makeUrlRelative("${pageContext.urlPrefix}/svg/${link.url.substring(6)}.svg", pageContext.url))
        }
        return link
    }

    class Factory(private val pageContext: AbstractPageContext) : LinkResolverFactory {
        override fun apply(context: LinkResolverBasicContext): LinkResolver {
            return CustomLinkResolver(pageContext)
        }

        override fun getAfterDependents(): MutableSet<Class<*>>? = null

        override fun getBeforeDependents(): MutableSet<Class<*>>? = null

        override fun affectsGlobalScope() = false
    }
}
