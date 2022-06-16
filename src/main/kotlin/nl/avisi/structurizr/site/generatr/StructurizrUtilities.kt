package nl.avisi.structurizr.site.generatr

import com.structurizr.Workspace
import com.structurizr.documentation.Documentation
import com.structurizr.documentation.Section
import com.structurizr.dsl.StructurizrDslParser
import com.structurizr.model.Element
import com.structurizr.model.Location
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import java.io.File

fun parseStructurizrDslWorkspace(workspaceFile: File) =
    StructurizrDslParser()
        .apply { parse(workspaceFile) }
        .workspace
        ?: throw IllegalStateException("Workspace could not be parsed")

val Documentation.homeSection: Section
    get() =
        sections.first { it.order == 1 }

val Element.normalizedName: String
    get() = name.normalize()

fun String.normalize(): String = lowercase().replace("\\s+".toRegex(), "-")

val Model.internalSoftwareSystems: List<SoftwareSystem>
    get() = softwareSystems.filter { it.location == Location.Internal }

fun Workspace.hasSystemContextViews(softwareSystem: SoftwareSystem) =
    views.systemContextViews.any { it.softwareSystem == softwareSystem }
