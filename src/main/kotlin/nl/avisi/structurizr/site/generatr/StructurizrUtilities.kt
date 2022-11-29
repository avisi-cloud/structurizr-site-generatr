package nl.avisi.structurizr.site.generatr

import com.structurizr.model.Location
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.ViewSet

val Model.includedSoftwareSystems: List<SoftwareSystem>
    get() = softwareSystems.filter { it.includedSoftwareSystem }

val SoftwareSystem.includedSoftwareSystem
    get() = this.location != Location.External

fun SoftwareSystem.hasDecisions() = documentation.decisions.isNotEmpty()

fun SoftwareSystem.hasDocumentationSections() = documentation.sections.size >= 2

fun ViewSet.hasSystemContextViews(softwareSystem: SoftwareSystem) =
    systemContextViews.any { it.softwareSystem == softwareSystem }

fun ViewSet.hasContainerViews(softwareSystem: SoftwareSystem) =
    containerViews.any { it.softwareSystem == softwareSystem }

fun ViewSet.hasComponentViews(softwareSystem: SoftwareSystem) =
    componentViews.any { it.softwareSystem == softwareSystem }

fun ViewSet.hasDeploymentViews(softwareSystem: SoftwareSystem) =
    deploymentViews.any { it.softwareSystem == softwareSystem }
