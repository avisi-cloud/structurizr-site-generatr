package nl.avisi.structurizr.site.generatr.site

import com.structurizr.Workspace
import com.structurizr.export.Diagram
import com.structurizr.export.IndentingWriter
import com.structurizr.export.plantuml.C4PlantUMLExporter
import com.structurizr.export.plantuml.StructurizrPlantUMLExporter
import com.structurizr.model.Component
import com.structurizr.model.Container
import com.structurizr.model.Element
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.*
import nl.avisi.structurizr.site.generatr.*

enum class ExporterType { C4, STRUCTURIZR }

class PlantUmlExporter(val workspace: Workspace) {
    private val exporter = when (workspace.exporterType()) {
        ExporterType.C4 -> C4PlantUMLExporter(ColorScheme.Light)
        ExporterType.STRUCTURIZR -> StructurizrPlantUMLExporter(ColorScheme.Light)
    }

    fun export(): Collection<Diagram> = exporter.export(workspace)
}

class PlantUmlExporterWithElementLinks(workspace: Workspace, url: String) {
    private val exporter = when (workspace.exporterType()) {
        ExporterType.C4 -> C4PlantUmlExporterWithElementLinks(workspace, url, ColorScheme.Light)
        ExporterType.STRUCTURIZR -> StructurizrPlantUmlExporterWithElementLinks(workspace, url, ColorScheme.Light)
    }

    init {
        if (workspace.views.configuration.properties.containsKey("generatr.svglink.target")) {
            exporter.addSkinParam(
                "svgLinkTarget",
                workspace.views.configuration.properties.getValue("generatr.svglink.target")
            )
        }
    }

    fun export(view: View): Diagram = when (view) {
        is CustomView -> exporter.export(view)
        is SystemLandscapeView -> exporter.export(view)
        is SystemContextView -> exporter.export(view)
        is ContainerView -> exporter.export(view)
        is ComponentView -> exporter.export(view)
        is DynamicView -> exporter.export(view)
        is DeploymentView -> exporter.export(view)
        else -> throw IllegalStateException("View ${view.name} has a non-exportable type")
    }
}

fun Workspace.exporterType() = views.configuration.properties
    .getOrDefault("generatr.site.exporter", "c4")
    .let { ExporterType.valueOf(it.uppercase()) }

private class WriterWithElementLinks(
    private val workspace: Workspace,
    private val url: String
) {
    companion object {
        const val TEMP_URI = "https://will-be-changed-to-relative/"
    }

    fun writeHeader(
        view: ModelView,
        writer: IndentingWriter,
        writeHeaderFn: (view: ModelView, writer: IndentingWriter) -> Unit
    ) {
        writeHeaderFn(view, writer)
        writer.writeLine("skinparam svgDimensionStyle false")
        writer.writeLine("skinparam preserveAspectRatio meet")
    }

    fun writeElement(
        view: ModelView?,
        element: Element?,
        writer: IndentingWriter?,
        writeElementFn: (view: ModelView?, element: Element?, writer: IndentingWriter?) -> Unit
    ) {
        val url = when {
            needsLinkToSoftwareSystem(element, view) -> getUrlToElement(element, "context")
            needsLinkToContainerViews(element, view, workspace) -> getUrlToElement(element)
            needsLinkToComponentViews(element, view, workspace) -> getUrlToElement(element)
            needsLinkToCodeViews(element, workspace) -> getUrlToElement(element)
            else -> null
        }

        if (url != null)
            writeElementWithCustomUrl(element, url, view, writer, writeElementFn)
        else
            writeElementFn(view, element, writer)
    }

    private fun needsLinkToSoftwareSystem(element: Element?, view: ModelView?) =
        element is SoftwareSystem && workspace.includedSoftwareSystems.contains(element) && element != view?.softwareSystem

    private fun needsLinkToContainerViews(element: Element?, view: ModelView?, workspace: Workspace) =
        element is SoftwareSystem && workspace.includedSoftwareSystems.contains(element) && element == view?.softwareSystem &&
            (element.hasContainers || workspace.hasImageViews(element.id))

    private fun needsLinkToComponentViews(element: Element?, view: ModelView?, workspace: Workspace) =
        element is Container && (element.hasComponents || workspace.hasImageViews(element.id)) && view !is ComponentView

    private fun needsLinkToCodeViews(element: Element?, workspace: Workspace) =
        element is Component && workspace.hasImageViews(element.id)

    private fun getUrlToElement(element: Element?, page: String? = null): String {
        val path = when (element) {
            is SoftwareSystem -> "/${element.name?.normalize()}/${page?.let { page } ?: "container"}/".asUrlToDirectory(url)
            is Container -> "/${element.parent?.name?.normalize()}/${page?.let { page } ?: "component"}/${element.name?.normalize()}".asUrlToDirectory(url)
            is Component -> "/${element.parent?.parent?.name?.normalize()}/${page?.let { page } ?: "code"}/${element.container?.name?.normalize()}/${element.name?.normalize()}".asUrlToDirectory(url)
            else -> throw IllegalStateException("Not supported element")
        }
        return "$TEMP_URI$path"
    }

    private fun writeElementWithCustomUrl(
        element: Element?,
        url: String?,
        view: ModelView?,
        writer: IndentingWriter?,
        writeElementFn: (view: ModelView?, element: Element?, writer: IndentingWriter?) -> Unit
    ) {
        element?.url = url
        writeModifiedElement(view, element, writer, writeElementFn)
        element?.url = null
    }

    private fun writeModifiedElement(
        view: ModelView?,
        element: Element?,
        writer: IndentingWriter?,
        writeElement: (view: ModelView?, element: Element?, writer: IndentingWriter?) -> Unit
    ) = IndentingWriter().let {
        writeElement(view, element, it)
        it.toString()
            .replace(TEMP_URI, "")
            .lines()
            .forEach { line -> writer?.writeLine(line) }
    }
}

class C4PlantUmlExporterWithElementLinks(workspace: Workspace, url: String, colorScheme: ColorScheme = ColorScheme.Light) : C4PlantUMLExporter(colorScheme) {
    private val withElementLinks = WriterWithElementLinks(workspace, url)

    override fun writeHeader(view: ModelView, writer: IndentingWriter) {
        withElementLinks.writeHeader(view, writer) { v, w -> super.writeHeader(v, w) }
    }

    override fun writeElement(view: ModelView?, element: Element?, writer: IndentingWriter?) {
        withElementLinks.writeElement(view, element, writer) { v, e, w -> super.writeElement(v, e, w) }
    }
}

class StructurizrPlantUmlExporterWithElementLinks(workspace: Workspace, url: String, colorScheme: ColorScheme = ColorScheme.Light) : StructurizrPlantUMLExporter(colorScheme) {
    private val withElementLinks = WriterWithElementLinks(workspace, url)

    override fun writeHeader(view: ModelView, writer: IndentingWriter) {
        withElementLinks.writeHeader(view, writer) { v, w -> super.writeHeader(v, w) }
    }

    override fun writeElement(view: ModelView?, element: Element?, writer: IndentingWriter?) {
        withElementLinks.writeElement(view, element, writer) { v, e, w -> super.writeElement(v, e, w) }
    }
}
