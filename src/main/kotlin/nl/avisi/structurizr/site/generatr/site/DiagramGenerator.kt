package nl.avisi.structurizr.site.generatr.site

import com.structurizr.Workspace
import com.structurizr.export.Diagram
import com.structurizr.export.plantuml.PlantUMLDiagram
import com.structurizr.view.ModelView
import com.structurizr.view.View
import net.sourceforge.plantuml.FileFormat
import net.sourceforge.plantuml.FileFormatOption
import net.sourceforge.plantuml.SourceStringReader
import java.io.ByteArrayOutputStream
import java.io.File
import java.net.URI
import java.util.concurrent.ConcurrentHashMap

val emptyPlantUmlDefinition = """
    @startuml
        hide uml
    @enduml
""".trimIndent()

fun generateDiagrams(workspace: Workspace, exportDir: File) {
    val pumlDir = pumlDir(exportDir)
    val svgDir = svgDir(exportDir)
    val pngDir = pngDir(exportDir)

    val plantUMLDiagrams = generatePlantUMLDiagrams(workspace)

    plantUMLDiagrams.parallelStream()
        .forEach { diagram ->
            val plantUMLFile = File(pumlDir, "${diagram.key}.puml")
            val plantUMLLegendFile = File(pumlDir, "${diagram.key}.legend.puml")

            val diagramNeedsUpdating = !plantUMLFile.exists() || plantUMLFile.readText() != diagram.definition
            val legendNeedsUpdating = !plantUMLLegendFile.exists() || plantUMLLegendFile.readText() != (diagram.legend?.definition ?: emptyPlantUmlDefinition)

            if (diagramNeedsUpdating || legendNeedsUpdating) {
                println("${diagram.key}...")
                if (diagramNeedsUpdating) {
                    diagram.withCachedIncludes().let { cachedDiagram ->
                        saveAsSvg(cachedDiagram.definition, svgDir, diagram.key)
                        saveAsPng(cachedDiagram.definition, pngDir, diagram.key)
                    }
                    saveAsPUML(diagram.definition, plantUMLFile)
                }
                if (legendNeedsUpdating) {
                    val diagramLegendDefinition = diagram.legend?.definition ?: emptyPlantUmlDefinition.trimIndent()

                    saveAsSvg(diagramLegendDefinition, svgDir,diagram.key + ".legend")
                    saveAsPng(diagramLegendDefinition, pngDir, diagram.key + ".legend")
                    saveAsPUML(diagramLegendDefinition, plantUMLLegendFile)
                }
            } else {
                println("${diagram.key} UP-TO-DATE")
            }
        }
}

fun generateDiagramWithElementLinks(
    workspace: Workspace,
    view: View,
    url: String,
    diagramCache: ConcurrentHashMap<String, Pair<String, String>>
): Pair<String, String> {
    val diagram = generatePlantUMLDiagramWithElementLinks(workspace, view, url)

    val name = "${diagram.key}-${view.key}"
    return diagramCache.getOrPut(name) {
        convertDefinitionToSvg(diagram.withCachedIncludes().definition) to
        convertDefinitionToSvg(diagram.legend?.definition ?: emptyPlantUmlDefinition)
    }
}

private fun convertDefinitionToSvg(definition: String): String {
    val reader = SourceStringReader(definition)
    val stream = ByteArrayOutputStream()

    reader.outputImage(stream, FileFormatOption(FileFormat.SVG, false))
    return stream.toString(Charsets.UTF_8)
}

private fun generatePlantUMLDiagrams(workspace: Workspace): Collection<Diagram> {
    val plantUMLExporter = PlantUmlExporter(workspace)

    return plantUMLExporter.export()
}

private fun saveAsPUML(definition: String, plantUMLFile: File) {
    plantUMLFile.writeText(definition)
}

private fun saveAsSvg(definition: String, svgDir: File, name: String) {
    val reader = SourceStringReader(definition)
    val svgFile = File(svgDir, "$name.svg")

    svgFile.outputStream().use {
        reader.outputImage(it, FileFormatOption(FileFormat.SVG, false))
    }
}

private fun saveAsPng(definition: String, pngDir: File, name: String) {
    val reader = SourceStringReader(definition)
    val pngFile = File(pngDir, "$name.png")

    pngFile.outputStream().use {
        reader.outputImage(it)
    }
}

private fun generatePlantUMLDiagramWithElementLinks(workspace: Workspace, view: View, url: String): Diagram {
    val plantUMLExporter = PlantUmlExporterWithElementLinks(workspace, url)

    return plantUMLExporter.export(view)
}

private fun pumlDir(exportDir: File) = File(exportDir, "puml").apply { mkdirs() }
private fun svgDir(exportDir: File) = File(exportDir, "svg").apply { mkdirs() }
private fun pngDir(exportDir: File) = File(exportDir, "png").apply { mkdirs() }

private fun Diagram.withCachedIncludes(): Diagram {
    val def = definition.replace("!include\\s+(.*)".toRegex()) {
        val cachedInclude = IncludeCache.cachedInclude(it.groupValues[1])
        "!include $cachedInclude"
    }

    return PlantUMLDiagram(view as ModelView, def)
}

private object IncludeCache {
    private val cache = mutableMapOf<String, String>()
    private val cacheDir = File("build/puml-cache").apply { mkdirs() }

    fun cachedInclude(includedFile: String): String {
        if (!includedFile.startsWith("http"))
            return includedFile

        return cache.getOrPut(includedFile) {
            val fileName = includedFile.split("/").last()
            val cachedFile = File(cacheDir, fileName)

            if (!cachedFile.exists())
                downloadIncludedFile(includedFile, cachedFile)

            cachedFile.absolutePath
        }
    }

    private fun downloadIncludedFile(includedFile: String, cachedFile: File) {
        URI(includedFile).toURL().openStream().use { inputStream ->
            cachedFile.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
    }
}
