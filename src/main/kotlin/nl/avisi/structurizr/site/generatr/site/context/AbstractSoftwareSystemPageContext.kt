package nl.avisi.structurizr.site.generatr.site.context

import com.structurizr.model.SoftwareSystem

abstract class AbstractSoftwareSystemPageContext(
    generatorContext: GeneratorContext,
    title: String,
    htmlFile: String
) : AbstractPageContext(generatorContext, title, htmlFile) {
    abstract val softwareSystem: SoftwareSystem

    val systemContextViews get() = workspace.views.systemContextViews.filter { it.softwareSystem == softwareSystem }
    val containerViews get() = workspace.views.containerViews.filter { it.softwareSystem == softwareSystem }
    val componentViews get() = workspace.views.componentViews.filter { it.softwareSystem == softwareSystem }
    val deploymentViews get() = workspace.views.deploymentViews.filter { it.softwareSystem == softwareSystem }
    val adrs get() = softwareSystem.documentation.decisions.toList()

    val showSystemContext get() = systemContextViews.isNotEmpty()
    val showContainer get() = containerViews.isNotEmpty()
    val showComponent get() = componentViews.isNotEmpty()
    val showDeployment get() = deploymentViews.isNotEmpty()
    val showAdr get() = adrs.isNotEmpty()
}
