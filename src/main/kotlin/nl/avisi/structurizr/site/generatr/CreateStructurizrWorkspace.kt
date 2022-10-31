package nl.avisi.structurizr.site.generatr

import com.structurizr.dsl.StructurizrDslParser
import com.structurizr.export.plantuml.C4PlantUMLExporter
import com.structurizr.model.Element
import java.io.File

fun createStructurizrWorkspace(workspaceFile: File) =
    StructurizrDslParser()
        .apply { parse(workspaceFile) }
        .workspace
        .apply {
            views.configuration.addProperty(C4PlantUMLExporter.C4PLANTUML_ELEMENT_PROPERTIES_PROPERTY, true.toString())
        }
        ?: throw IllegalStateException("Workspace could not be parsed")
