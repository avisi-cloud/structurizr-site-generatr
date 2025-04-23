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
import kotlin.math.exp

fun generateDiagrams(workspace: Workspace, exportDir: File) {
    val pumlDir = pumlDir(exportDir)
    val svgDir = svgDir(exportDir)
    val pngDir = pngDir(exportDir)
    val d2Dir = d2Dir(exportDir)

    val diagrams = generateDiagrams(workspace)
    val exporterType = workspace.exporterType()

    diagrams.parallelStream()
        .forEach { diagram ->
            val plantUMLFile = File(pumlDir, "${diagram.key}.puml")
            if (!plantUMLFile.exists() || plantUMLFile.readText() != diagram.definition) {
                println("${diagram.key}...")
                when (exporterType) {
                    ExporterType.D2 -> saveAsD2(diagram, d2Dir)
                    ExporterType.C4, ExporterType.STRUCTURIZR -> {
                        saveAsPng(diagram, pngDir)
                        saveAsSvg(diagram, svgDir)
                    }
                }

                saveAsPUML(diagram, plantUMLFile)
            } else {
                println("${diagram.key} UP-TO-DATE")
            }
        }
}

fun generateDiagramWithElementLinks(
    workspace: Workspace,
    view: View,
    url: String,
    diagramCache: ConcurrentHashMap<String, String>
): String {
    val diagram = generateDiagramWithElementLinks(workspace, view, url)

    val name = "${diagram.key}-${view.key}"

    return diagramCache.getOrPut(name) {
        when (workspace.exporterType()) {
            ExporterType.D2 -> diagram.definition
            ExporterType.C4, ExporterType.STRUCTURIZR -> {
                val reader = SourceStringReader(diagram.withCachedIncludes().definition)
                val stream = ByteArrayOutputStream()

                reader.outputImage(stream, FileFormatOption(FileFormat.SVG, false))
                stream.toString(Charsets.UTF_8)
            }
        }
    }
}

private fun generateDiagrams(workspace: Workspace): Collection<Diagram> {
    val diagramExporter = DiagramExporter(workspace)

    return diagramExporter.export()
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

private fun saveAsD2(diagram: Diagram, d2Dir: File, name: String = diagram.key) {
    val d2File = File(d2Dir, "${name}.d2")
    d2File.writeText(diagram.definition)
}

private fun saveAsPng(diagram: Diagram, pngDir: File) {
    val reader = SourceStringReader(diagram.withCachedIncludes().definition)
    val pngFile = File(pngDir, "${diagram.key}.png")

    pngFile.outputStream().use {
        reader.outputImage(it)
    }
}

private fun generateDiagramWithElementLinks(workspace: Workspace, view: View, url: String): Diagram {
    val diagramExporter = DiagramExporterWithElementLinks(workspace, url)

    return diagramExporter.export(view)
}

private fun pumlDir(exportDir: File) = File(exportDir, "puml").apply { mkdirs() }
private fun svgDir(exportDir: File) = File(exportDir, "svg").apply { mkdirs() }
private fun pngDir(exportDir: File) = File(exportDir, "png").apply { mkdirs() }
private fun d2Dir(exportDir: File) = File(exportDir, "d2").apply { mkdirs() }

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
