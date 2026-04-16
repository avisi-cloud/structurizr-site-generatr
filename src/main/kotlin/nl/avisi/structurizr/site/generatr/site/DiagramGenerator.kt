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

fun generateDiagrams(workspace: Workspace, exportDir: File) {
    val pumlDir = pumlDir(exportDir)
    val svgDir = svgDir(exportDir)
    val pngDir = pngDir(exportDir)

    val plantUMLDiagrams = generatePlantUMLDiagrams(workspace)

    plantUMLDiagrams.parallelStream()
        .forEach { diagram ->
            val plantUMLFile = File(pumlDir, "${diagram.key}.puml")
            if (!plantUMLFile.exists() || plantUMLFile.readText() != diagram.definition) {
                println("${diagram.key}...")
                saveAsSvg(diagram, svgDir)
                saveAsPng(diagram, pngDir)
                saveAsPUML(diagram, plantUMLFile)
            } else {
                println("${diagram.key} UP-TO-DATE")
            }

            val legend = diagram.legend
            if (legend != null) {
                val legendPumlFile = File(pumlDir, "${diagram.key}.legend.puml")
                if (!legendPumlFile.exists() || legendPumlFile.readText() != legend.definition) {
                    println("${diagram.key}.legend...")
                    saveLegendAsSvg(legend.definition, diagram.key, svgDir)
                    saveLegendAsPng(legend.definition, diagram.key, pngDir)
                    legendPumlFile.writeText(legend.definition)
                } else {
                    println("${diagram.key}.legend UP-TO-DATE")
                }
            }
        }
}

fun generateDiagramWithElementLinks(
    workspace: Workspace,
    view: View,
    url: String,
    diagramCache: ConcurrentHashMap<String, String>
): String {
    val diagram = generatePlantUMLDiagramWithElementLinks(workspace, view, url)

    val name = "${diagram.key}-${view.key}"
    return diagramCache.getOrPut(name) {
        val reader = SourceStringReader(diagram.withCachedIncludes().definition)
        val stream = ByteArrayOutputStream()

        reader.outputImage(stream, FileFormatOption(FileFormat.SVG, false))
        stream.toString(Charsets.UTF_8)
    }
}

private fun generatePlantUMLDiagrams(workspace: Workspace): Collection<Diagram> {
    val plantUMLExporter = PlantUmlExporter(workspace)

    return plantUMLExporter.export()
}

private fun saveLegendAsSvg(legendDefinition: String, diagramKey: String, svgDir: File) {
    val reader = SourceStringReader(legendDefinition)
    val svgFile = File(svgDir, "$diagramKey.legend.svg")

    svgFile.outputStream().use {
        reader.outputImage(it, FileFormatOption(FileFormat.SVG, false))
    }
}

private fun saveLegendAsPng(legendDefinition: String, diagramKey: String, pngDir: File) {
    val reader = SourceStringReader(legendDefinition)
    val pngFile = File(pngDir, "$diagramKey.legend.png")

    pngFile.outputStream().use {
        reader.outputImage(it)
    }
}

private fun saveAsPUML(diagram: Diagram, plantUMLFile: File) {
    plantUMLFile.writeText(diagram.definition)
}

private fun saveAsSvg(diagram: Diagram, svgDir: File, name: String = diagram.key) {
    val reader = SourceStringReader(diagram.withCachedIncludes().definition)
    val svgFile = File(svgDir, "$name.svg")

    svgFile.outputStream().use {
        reader.outputImage(it, FileFormatOption(FileFormat.SVG, false))
    }
}

private fun saveAsPng(diagram: Diagram, pngDir: File) {
    val reader = SourceStringReader(diagram.withCachedIncludes().definition)
    val pngFile = File(pngDir, "${diagram.key}.png")

    pngFile.outputStream().use {
        reader.outputImage(it)
    }
}

fun generateLegendSvgs(workspace: Workspace): Map<String, String> {
    val diagrams = generatePlantUMLDiagrams(workspace)
    val legendSvgs = ConcurrentHashMap<String, String>()

    diagrams.parallelStream().forEach { diagram ->
        val legend = diagram.legend
        if (legend != null) {
            val reader = SourceStringReader(legend.definition)
            val stream = ByteArrayOutputStream()
            reader.outputImage(stream, FileFormatOption(FileFormat.SVG, false))
            legendSvgs[diagram.key] = stream.toString(Charsets.UTF_8)
        }
    }

    return legendSvgs
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
