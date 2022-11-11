package nl.avisi.structurizr.site.generatr.site

import com.structurizr.Workspace
import com.structurizr.export.Diagram
import com.structurizr.export.plantuml.PlantUMLDiagram
import net.sourceforge.plantuml.FileFormat
import net.sourceforge.plantuml.FileFormatOption
import net.sourceforge.plantuml.SourceStringReader
import java.io.File
import java.net.URL

fun generateDiagrams(workspace: Workspace, exportDir: File, branch: String) {
    val pumlDir = File(exportDir, "puml").apply { mkdirs() }
    val pngDir = File(exportDir, "png").apply { mkdirs() }
    val svgDir = File(exportDir, "svg").apply { mkdirs() }

    val plantUMLDiagrams = generatePlantUMLDiagrams(workspace, branch)

    plantUMLDiagrams.parallelStream()
        .forEach { diagram ->
            val plantUMLFile = File(pumlDir, "${diagram.key}.puml")
            if (!plantUMLFile.exists() || plantUMLFile.readText() != diagram.definition) {
                println("${diagram.key}...")
                saveAsPUML(diagram, plantUMLFile)
                saveImages(diagram, pngDir, svgDir)
            } else {
                println("${diagram.key} UP-TO-DATE")
            }
        }
}

private fun generatePlantUMLDiagrams(workspace: Workspace, branch: String): Collection<Diagram> {
    val plantUMLExporter = C4PlantUmlExporterWithElementLinks(workspace, branch)

    return plantUMLExporter.export(workspace)
}

private fun saveAsPUML(diagram: Diagram, plantUMLFile: File) {
    plantUMLFile.writeText(diagram.definition)
}

private fun saveImages(diagram: Diagram, pngDir: File, svgDir: File) {
    val reader = SourceStringReader(diagram.withCachedIncludes().definition)
    val pngFile = File(pngDir, "${diagram.key}.png")
    val svgFile = File(svgDir, "${diagram.key}.svg")

    pngFile.outputStream().use {
        reader.outputImage(it)
    }
    svgFile.outputStream().use {
        reader.outputImage(it, FileFormatOption(FileFormat.SVG, false))
    }
}

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
