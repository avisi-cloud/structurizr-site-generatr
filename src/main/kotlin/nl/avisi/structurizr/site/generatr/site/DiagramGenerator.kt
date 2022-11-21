package nl.avisi.structurizr.site.generatr.site

import com.structurizr.Workspace
import com.structurizr.export.Diagram
import com.structurizr.export.plantuml.C4PlantUMLExporter
import com.structurizr.export.plantuml.PlantUMLDiagram
import com.structurizr.view.View
import net.sourceforge.plantuml.FileFormat
import net.sourceforge.plantuml.FileFormatOption
import net.sourceforge.plantuml.SourceStringReader
import nl.avisi.structurizr.site.generatr.site.C4PlantUmlExporterWithElementLinks.Companion.export
import java.io.File
import java.net.URL

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
                saveAsPUML(diagram, plantUMLFile)
                saveAsSvg(diagram, svgDir)
                saveAsPng(diagram, pngDir)
            } else {
                println("${diagram.key} UP-TO-DATE")
            }
        }
}

fun generateDiagramWithElementLinks(workspace: Workspace, view: View, url: String, exportDir: File): String {
    val pumlDir = pumlDir(exportDir)
    val svgDir = svgDir(exportDir)

    val diagram = generatePlantUMLDiagramWithElementLinks(workspace, view, url)

    val name = "${diagram.key}-${view.key}"
    val plantUMLFile = File(pumlDir, "$name.puml")
    if (!plantUMLFile.exists() || plantUMLFile.readText() != diagram.definition) {
        saveAsPUML(diagram, plantUMLFile)
        saveAsSvg(diagram, svgDir, name)
    } else {
        println("$name UP-TO-DATE")
    }

    return readSvg(svgDir, name)
}

private fun generatePlantUMLDiagrams(workspace: Workspace): Collection<Diagram> {
    val plantUMLExporter = C4PlantUMLExporter()

    return plantUMLExporter.export(workspace)
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

private fun readSvg(svgDir: File, name: String): String {
    val svgFile = File(svgDir, "$name.svg")
    return svgFile.readText()
}

private fun generatePlantUMLDiagramWithElementLinks(workspace: Workspace, view: View, url: String): Diagram {
    val plantUMLExporter = C4PlantUmlExporterWithElementLinks(workspace, url)

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

    return PlantUMLDiagram(view, def)
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
        URL(includedFile).openStream().use { inputStream ->
            cachedFile.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
    }
}
