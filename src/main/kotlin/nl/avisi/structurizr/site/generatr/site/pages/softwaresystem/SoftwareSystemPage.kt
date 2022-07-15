package nl.avisi.structurizr.site.generatr.site.pages.softwaresystem

import kotlinx.html.*
import nl.avisi.structurizr.site.generatr.site.components.*
import nl.avisi.structurizr.site.generatr.site.context.*

fun HTML.softwareSystemPage(context: AbstractSoftwareSystemPageContext) {
    softwareSystemPage(context) {
        when (context) {
            is SoftwareSystemDeploymentPageContext -> deploymentFragment(context)
            is SoftwareSystemDecisionsPageContext -> decisionListFragment(context)
            is SoftwareSystemDependenciesPageContext -> dependenciesFragment(context)
        }
    }
}

fun HTML.softwareSystemPage(context: AbstractSoftwareSystemPageContext, block: DIV.() -> Unit) {
    page(context) {
        h1(classes = "title mt-3") { +context.softwareSystem.name }
        h2(classes = "subtitle") { +context.softwareSystem.description }

        softwareSystemPageTabs(context)
        block()
    }
}

private fun DIV.softwareSystemPageTabs(context: AbstractSoftwareSystemPageContext) {
    tabs {
        if (context.showSystemContext)
            tab(
                href = SoftwareSystemContextPageContext(
                    context.generatorContext,
                    context.softwareSystem
                ).urlRelativeTo(context),
                active = context is SoftwareSystemContextPageContext
            ) { +"System context" }

        if (context.showContainer)
            tab(
                href = SoftwareSystemContainerPageContext(
                    context.generatorContext,
                    context.softwareSystem
                ).urlRelativeTo(context),
                active = context is SoftwareSystemContainerPageContext
            ) { +"Container views" }

        if (context.showComponent)
            tab(
                href = SoftwareSystemComponentPageContext(
                    context.generatorContext,
                    context.softwareSystem
                ).urlRelativeTo(context),
                active = context is SoftwareSystemComponentPageContext
            ) { +"Component views" }

        if (context.showDeployment)
            tab(
                href = SoftwareSystemDeploymentPageContext(
                    context.generatorContext,
                    context.softwareSystem
                ).urlRelativeTo(context),
                active = context is SoftwareSystemDeploymentPageContext
            ) { +"Deployment views" }

        tab(
            href = SoftwareSystemDependenciesPageContext(
                context.generatorContext,
                context.softwareSystem
            ).urlRelativeTo(context),
            active = context is SoftwareSystemDependenciesPageContext
        )
        { +"Dependencies" }

        if (context.showAdr)
            tab(
                href = SoftwareSystemDecisionsPageContext(
                    context.generatorContext,
                    context.softwareSystem
                ).urlRelativeTo(context),
                active = context is SoftwareSystemDecisionsPageContext || context is SoftwareSystemDecisionPageContext
            ) { +"Decisions" }
    }
}
