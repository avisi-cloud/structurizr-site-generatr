package nl.avisi.structurizr.site.generatr.site.components

import com.structurizr.model.SoftwareSystem
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
                val menuItemContext = createMenuItemContext(context, it)
                val currentSoftwareSystem = (context as? AbstractSoftwareSystemPageContext)?.softwareSystem
                val classes = if (currentSoftwareSystem == menuItemContext.softwareSystem) "is-active" else ""
                li {
                    a(href = menuItemContext.url, classes = classes) { +it.name }
                }
            }
    }
}

private fun createMenuItemContext(
    context: AbstractPageContext,
    softwareSystem: SoftwareSystem
): AbstractSoftwareSystemPageContext {
    if (context.workspace.views.systemContextViews.any { it.softwareSystem == softwareSystem })
        return SoftwareSystemContextPageContext(context.generatorContext, softwareSystem)
    if (context.workspace.views.containerViews.any { it.softwareSystem == softwareSystem })
        return SoftwareSystemContainerPageContext(context.generatorContext, softwareSystem)
    if (context.workspace.views.componentViews.any { it.softwareSystem == softwareSystem })
        return SoftwareSystemComponentPageContext(context.generatorContext, softwareSystem)
    if (context.workspace.views.deploymentViews.any { it.softwareSystem == softwareSystem })
        return SoftwareSystemDeploymentPageContext(context.generatorContext, softwareSystem)

    return SoftwareSystemDependenciesPageContext(context.generatorContext, softwareSystem)
}
