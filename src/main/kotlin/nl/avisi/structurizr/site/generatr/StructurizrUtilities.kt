package nl.avisi.structurizr.site.generatr

import com.structurizr.Workspace
import com.structurizr.model.Container
import com.structurizr.model.Location
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.ImageView
import com.structurizr.view.ViewSet
import nl.avisi.structurizr.site.generatr.site.GeneratorContext

val Workspace.includedSoftwareSystems: List<SoftwareSystem>
    get() = model.softwareSystems.filter {
        val externalTag = views.configuration.properties.getOrDefault("generatr.site.externalTag", null)
        it.location != Location.External && if (externalTag != null) !it.tags.contains(externalTag) else true
    }

val SoftwareSystem.hasContainers
    get() = this.containers.isNotEmpty()

val SoftwareSystem.includedProperties
    get() = this.properties.filterNot { (name, _) -> name == "structurizr.dsl.identifier" }

val Container.hasComponents
    get() = this.components.isNotEmpty()

fun SoftwareSystem.hasDecisions() = documentation.decisions.isNotEmpty()

fun SoftwareSystem.hasContainerDecisions() = containers.any { it.hasDecisions() }

fun SoftwareSystem.hasDocumentationSections() = documentation.sections.size >= 2

fun SoftwareSystem.hasContainerDocumentationSections() = containers.any { it.hasSections() }

fun Container.hasDecisions() = documentation.decisions.isNotEmpty()

fun Container.hasSections() = documentation.sections.isNotEmpty()

fun ViewSet.hasSystemContextViews(softwareSystem: SoftwareSystem) =
    systemContextViews.any { it.softwareSystem == softwareSystem }

fun ViewSet.hasContainerViews(generatorContext: GeneratorContext, softwareSystem: SoftwareSystem) =
    containerViews.any { it.softwareSystem == softwareSystem } || getImagesForSystem(generatorContext.workspace, softwareSystem).isNotEmpty()

fun ViewSet.hasComponentViews(generatorContext: GeneratorContext, softwareSystem: SoftwareSystem) =
    componentViews.any { it.softwareSystem == softwareSystem } || getImagesForContainer(generatorContext.workspace, softwareSystem).isNotEmpty()

fun ViewSet.hasCodeViews(generatorContext: GeneratorContext, softwareSystem: SoftwareSystem) =
    componentViews.any { it.softwareSystem == softwareSystem } || getImagesForComponent(generatorContext, softwareSystem).isNotEmpty()

fun ViewSet.hasDynamicViews(softwareSystem: SoftwareSystem) =
    dynamicViews.any { it.softwareSystem == softwareSystem }

fun ViewSet.hasDeploymentViews(softwareSystem: SoftwareSystem) =
    deploymentViews.any { it.softwareSystem == softwareSystem }

fun getImageViewsForId(workspace: Workspace, id: String): List<ImageView> {
    return workspace.views.imageViews
        .filter { it.elementId == id }
        .sortedBy { it.key }
}

fun getImagesForSystem(workspace: Workspace, softwareSystem: SoftwareSystem): List<ImageView> {
    return workspace.views.imageViews
        .filter { it.elementId == softwareSystem.id }
        .sortedBy { it.key }
}

fun getImagesForContainer(workspace: Workspace, softwareSystem: SoftwareSystem): List<ImageView> {
    val images = mutableListOf<ImageView>()
    softwareSystem.containers.forEach{ container ->
        images += workspace.views.imageViews
            .filter { it.elementId == container.id }
    }
    return images
}

fun getImagesForComponent(generatorContext: GeneratorContext, softwareSystem: SoftwareSystem): List<ImageView> {
    val images = mutableListOf<ImageView>()
    softwareSystem.containers.forEach { container ->
        container.components.forEach { component ->
            images += generatorContext.workspace.views.imageViews
                .filter { it.elementId == component.id }
        }
    }
    return images
}
