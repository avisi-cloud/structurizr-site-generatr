package nl.avisi.structurizr.site.generatr.site.factory

import com.structurizr.Workspace
import com.structurizr.export.plantuml.AbstractPlantUMLExporter
import com.structurizr.export.plantuml.C4PlantUMLExporter
import com.structurizr.export.plantuml.StructurizrPlantUMLExporter
import nl.avisi.structurizr.site.generatr.site.C4PlantUmlExporterWithElementLinks
import nl.avisi.structurizr.site.generatr.site.StructurizrPlantUmlExporterWithElementLinks
import java.util.*

class PlantUMLExporterFactory {
    fun makeExporter(type: String): AbstractPlantUMLExporter {
        return when(type.lowercase(Locale.getDefault())) {
            "c4" -> C4PlantUMLExporter()
            "structurizr" -> StructurizrPlantUMLExporter()
            else -> throw Exception("unknown diagram exporter type")
        }
    }

    fun makeExporterWithLinks(type: String, workspace: Workspace, url: String): AbstractPlantUMLExporter {
        return when(type.lowercase(Locale.getDefault())) {
            "c4" -> C4PlantUmlExporterWithElementLinks(workspace,url)
            "structurizr" -> StructurizrPlantUmlExporterWithElementLinks(workspace, url)
            else -> throw Exception("unknown diagram exporter type")
        }
    }
}
