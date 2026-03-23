package nl.avisi.structurizr.site.generatr.site

import com.structurizr.Workspace
import com.structurizr.export.Diagram
import com.structurizr.export.plantuml.PlantUMLDiagram
import com.structurizr.view.ColorScheme
import com.structurizr.view.ModelView
import com.structurizr.view.View
import net.sourceforge.plantuml.FileFormat
import net.sourceforge.plantuml.FileFormatOption
import net.sourceforge.plantuml.SourceStringReader
import nl.avisi.structurizr.site.generatr.* // <--- FIXED: Star import to find ColorScheme
import java.io.ByteArrayOutputStream
import java.io.File
import java.net.URI
import java.util.concurrent.ConcurrentHashMap

fun generateDiagrams(workspace: Workspace, exportDir: File) {
    val pumlDir = pumlDir(exportDir)
    val svgDir = svgDir(exportDir)
    val pngDir = pngDir(exportDir)

    // 1. Generate Light Mode Diagrams (Default)
    generatePlantUMLDiagrams(workspace, ColorScheme.Light).parallelStream()
        .forEach { diagram ->
            processDiagram(diagram, pumlDir, svgDir, pngDir, suffix = "")
        }

    // 2. Generate Dark Mode Diagrams (With "-dark" suffix)
    generatePlantUMLDiagrams(workspace, ColorScheme.Dark).parallelStream()
        .forEach { diagram ->
            processDiagram(diagram, pumlDir, svgDir, pngDir, suffix = "-dark")
        }
}

private fun processDiagram(
    diagram: Diagram,
    pumlDir: File,
    svgDir: File,
    pngDir: File,
    suffix: String
) {
    val name = "${diagram.key}$suffix"
    val plantUMLFile = File(pumlDir, "$name.puml")
    
    // Check if the file content has changed or if it doesn't exist
    if (!plantUMLFile.exists() || plantUMLFile.readText() != diagram.definition) {
        println("$name...")
        saveAsSvg(diagram, svgDir, name)
        saveAsPng(diagram, pngDir, name)
        saveAsPUML(diagram, plantUMLFile)
    } else {
        println("$name UP-TO-DATE")
    }
}

fun generateDiagramWithElementLinks(
    workspace: Workspace,
    view: View,
    url: String,
    diagramCache: ConcurrentHashMap<String, String>,
    colorScheme: ColorScheme = ColorScheme.Light
): String {
    val diagram = generatePlantUMLDiagramWithElementLinks(workspace, view, url, colorScheme)

    // Include color scheme in cache key to separate Light/Dark versions
    val name = "${diagram.key}-${view.key}-${colorScheme.name.lowercase()}"
    return diagramCache.getOrPut(name) {
        val reader = SourceStringReader(diagram.withCachedIncludes().definition)
        val stream = ByteArrayOutputStream()

        reader.outputImage(stream, FileFormatOption(FileFormat.SVG, false))
        stream.toString(Charsets.UTF_8)
    }
}

private fun generatePlantUMLDiagrams(workspace: Workspace, colorScheme: ColorScheme): Collection<Diagram> {
    val plantUMLExporter = PlantUmlExporter(workspace)
    return plantUMLExporter.export(colorScheme)
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

private fun saveAsPng(diagram: Diagram, pngDir: File, name: String = diagram.key) {
    val reader = SourceStringReader(diagram.withCachedIncludes().definition)
    val pngFile = File(pngDir, "$name.png")

    pngFile.outputStream().use {
        reader.outputImage(it)
    }
}

private fun generatePlantUMLDiagramWithElementLinks(
    workspace: Workspace, 
    view: View, 
    url: String, 
    colorScheme: ColorScheme
): Diagram {
    val plantUMLExporter = PlantUmlExporterWithElementLinks(workspace, url, colorScheme)
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