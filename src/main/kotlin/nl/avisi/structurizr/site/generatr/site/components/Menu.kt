package nl.avisi.structurizr.site.generatr.site.components

import kotlinx.html.*
import nl.avisi.structurizr.site.generatr.homeSection
import nl.avisi.structurizr.site.generatr.internalSoftwareSystems
import nl.avisi.structurizr.site.generatr.site.context.*
import nl.avisi.structurizr.site.generatr.site.context.SoftwareSystemsOverviewPageContext

fun DIV.menu(context: AbstractPageContext) {
    aside(classes = "menu p-3") {
        generalSection(context)
        softwareSystemsSection(context)
    }
}

private fun ASIDE.generalSection(context: AbstractPageContext) {
    p(classes = "menu-label") { +"General" }
    ul(classes = "menu-list") {
        li {
            a(
                href = HomePageContext(context.generatorContext, context.workspace.documentation.homeSection).url,
                classes = if (context is HomePageContext) "is-active" else ""
            ) { +"Home" }
            a(
                href = SoftwareSystemsOverviewPageContext(context.generatorContext).url,
                classes = if (context is SoftwareSystemsOverviewPageContext) "is-active" else ""
            ) { +"Software Systems" }

            context.workspace.documentation.sections
                .filter { it != context.workspace.documentation.homeSection }
                .sortedBy { it.order }
                .forEach {
                    val documentationPageContext = DocumentationSectionPageContext(context.generatorContext, it)
                    val isActiveSection = context is DocumentationSectionPageContext
                            && context.section == documentationPageContext.section
                    a(href = documentationPageContext.url, classes = if (isActiveSection) "is-active" else "") {
                        +it.title
                    }
                }
        }
    }
}

private fun ASIDE.softwareSystemsSection(context: AbstractPageContext) {
    p(classes = "menu-label") { +"Software systems" }
    ul(classes = "menu-list") {
        context.workspace.model.internalSoftwareSystems
            .sortedBy { it.name.lowercase() }
            .forEach {
                val info = SoftwareSystemContextPageContext(context.generatorContext, it)
                val currentSoftwareSystem = (context as? AbstractSoftwareSystemPageContext)?.softwareSystem
                val classes = if (currentSoftwareSystem == info.softwareSystem) "is-active" else ""
                li {
                    a(href = info.url, classes = classes) { +it.name }
                }
            }
    }
}
