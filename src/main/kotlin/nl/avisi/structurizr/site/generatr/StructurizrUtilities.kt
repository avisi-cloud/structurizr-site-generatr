package nl.avisi.structurizr.site.generatr

import com.structurizr.Workspace
import com.structurizr.model.Container
import com.structurizr.model.Location
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.ViewSet

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

fun ViewSet.hasContainerViews(softwareSystem: SoftwareSystem) =
    containerViews.any { it.softwareSystem == softwareSystem }

fun ViewSet.hasComponentViews(softwareSystem: SoftwareSystem) =
    componentViews.any { it.softwareSystem == softwareSystem }

fun ViewSet.hasDynamicViews(softwareSystem: SoftwareSystem) =
    dynamicViews.any { it.softwareSystem == softwareSystem }

fun ViewSet.hasDeploymentViews(softwareSystem: SoftwareSystem) =
    deploymentViews.any { it.softwareSystem == softwareSystem }
