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
            model.elements.forEach {
                moveUrlToProperty(it) // We need the URL later for our own links, preserve the original in a property
            }
        }
        ?: throw IllegalStateException("Workspace could not be parsed")

private fun moveUrlToProperty(element: Element) {
    if (element.url != null) {
        element.addProperty("Url", element.url)
        element.url = null
    }
}
