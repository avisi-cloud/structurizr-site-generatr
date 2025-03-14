package nl.avisi.structurizr.site.generatr

import com.structurizr.Workspace
import com.structurizr.model.Component
import com.structurizr.model.Container
import com.structurizr.model.SoftwareSystem
import com.structurizr.model.StaticStructureElement
import com.structurizr.view.ViewSet
import nl.avisi.structurizr.site.generatr.site.GeneratorContext

val Workspace.includedSoftwareSystems: List<SoftwareSystem>
    get() = model.softwareSystems.filter {
        val externalTag = views.configuration.properties.getOrDefault("generatr.site.externalTag", null)
        if (externalTag != null) !it.tags.contains(externalTag) else true
    }

fun Workspace.hasImageViews(id: String) = views.imageViews.any { it.elementId == id }

fun Workspace.hasComponentDiagrams(container: Container) = views.componentViews.any { it.container == container}

val SoftwareSystem.hasContainers
    get() = this.containers.isNotEmpty()

val StaticStructureElement.includedProperties
    get() = this.properties.filterNot { (name, _) -> name.startsWith("structurizr.") or name.startsWith("generatr.") }

val Container.hasComponents
    get() = this.components.isNotEmpty()

fun SoftwareSystem.firstContainer(generatorContext: GeneratorContext) = containers
        .sortedBy { it.name }.firstOrNull { container ->
            generatorContext.workspace.hasComponentDiagrams(container) or
                    generatorContext.workspace.hasImageViews(container.id) }

fun SoftwareSystem.hasDecisions() = documentation.decisions.isNotEmpty()

fun SoftwareSystem.hasContainerDecisions() = containers.any { it.hasDecisions() }

fun SoftwareSystem.hasDocumentationSections(recursive: Boolean = false) = documentation.sections.size >= 2 || (recursive && hasContainerDocumentationSections(recursive))

fun SoftwareSystem.hasContainerDocumentationSections(recursive: Boolean = false) = containers.any { it.hasSections(recursive) } || (recursive && hasComponentDocumentationSections())

fun SoftwareSystem.hasComponentDocumentationSections() = containers.any { it.hasComponentsSections() }

fun Container.firstComponent(generatorContext: GeneratorContext) = components
    .sortedBy { it.name }.firstOrNull {
        component -> generatorContext.workspace.hasImageViews(component.id) }

fun Container.hasDecisions() = documentation.decisions.isNotEmpty()

fun Container.hasSections(recursive: Boolean = false) = documentation.sections.isNotEmpty() || (recursive && hasComponentsSections())

fun Container.hasComponentsSections() = components.any { it.hasSections() }

fun Component.hasSections() = documentation.sections.isNotEmpty()

fun ViewSet.hasSystemContextViews(softwareSystem: SoftwareSystem) =
    systemContextViews.any { it.softwareSystem == softwareSystem }

fun ViewSet.hasContainerViews(workspace: Workspace, softwareSystem: SoftwareSystem) =
    containerViews.any { it.softwareSystem == softwareSystem } ||
        workspace.views.imageViews.any { it.elementId == softwareSystem.id }

fun ViewSet.hasComponentViews(workspace: Workspace, softwareSystem: SoftwareSystem) =
    componentViews.any { it.softwareSystem == softwareSystem } ||
        workspace.views.imageViews.any { it.elementId in softwareSystem.containers.map { cn -> cn.id } }

fun ViewSet.hasCodeViews(workspace: Workspace, softwareSystem: SoftwareSystem) =
    componentViews.any { it.softwareSystem == softwareSystem } &&
        workspace.views.imageViews.any { it.elementId in softwareSystem.containers.flatMap { cn -> cn.components.map { com -> com.id } } }

fun ViewSet.hasDynamicViews(softwareSystem: SoftwareSystem) =
    dynamicViews.any { it.softwareSystem == softwareSystem }

fun ViewSet.hasDeploymentViews(softwareSystem: SoftwareSystem) = with(deploymentViews) {
    any { it.softwareSystem == softwareSystem } || any { it.properties["generatr.view.deployment.belongsTo"] == softwareSystem.name }
}
