package nl.avisi.structurizr.site.generatr

import com.structurizr.dsl.StructurizrDslParser
import com.structurizr.model.Location
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import java.io.File

fun parseStructurizrDslWorkspace(workspaceFile: File) =
    StructurizrDslParser()
        .apply { parse(workspaceFile) }
        .workspace
        ?: throw IllegalStateException("Workspace could not be parsed")

val Model.includedSoftwareSystems: List<SoftwareSystem>
    get() = softwareSystems.filter { it.location != Location.External }
