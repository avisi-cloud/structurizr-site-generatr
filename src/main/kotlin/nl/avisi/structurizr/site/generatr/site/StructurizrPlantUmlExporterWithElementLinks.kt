package nl.avisi.structurizr.site.generatr.site

import com.structurizr.Workspace
import com.structurizr.export.Diagram
import com.structurizr.export.IndentingWriter
import com.structurizr.export.plantuml.AbstractPlantUMLExporter
import com.structurizr.export.plantuml.StructurizrPlantUMLExporter
import com.structurizr.model.Container
import com.structurizr.model.Element
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.*
import nl.avisi.structurizr.site.generatr.*

class StructurizrPlantUmlExporterWithElementLinks(
    private val workspace: Workspace,
    private val url: String
) : StructurizrPlantUMLExporter() {
    companion object {
        const val TEMP_URI = "https://will-be-changed-to-relative/"

        fun AbstractPlantUMLExporter.export(view: View): Diagram = when (view) {
            is CustomView -> export(view)
            is SystemLandscapeView -> export(view)
            is SystemContextView -> export(view)
            is ContainerView -> export(view)
            is ComponentView -> export(view)
            is DynamicView -> export(view)
            is DeploymentView -> export(view)
            else -> throw IllegalStateException("View ${view.name} has a non-exportable type")
        }
    }

    override fun writeHeader(view: ModelView, writer: IndentingWriter) {
        super.writeHeader(view, writer)
        writer.writeLine("skinparam svgDimensionStyle false")
        writer.writeLine("skinparam preserveAspectRatio meet")
    }

    override fun writeElement(view: ModelView?, element: Element?, writer: IndentingWriter?) {
        val url = when {
            needsLinkToSoftwareSystem(element, view) -> getUrlToSoftwareSystem(element)
            needsLinkToContainerViews(element, view) -> getUrlToContainerViews(element)
            needsLinkToComponentViews(element, view) -> getUrlToComponentViews(element)
            else -> null
        }

        if (url != null)
            writeElementWithCustomUrl(element, url, view, writer)
        else
            super.writeElement(view, element, writer)
    }

    private fun needsLinkToSoftwareSystem(element: Element?, view: ModelView?) =
        element is SoftwareSystem && workspace.includedSoftwareSystems.contains(element) && element != view?.softwareSystem

    private fun getUrlToSoftwareSystem(element: Element?): String {
        val path = "/${element?.name?.normalize()}/context/".asUrlToDirectory(url)
        return "$TEMP_URI$path"
    }

    private fun needsLinkToContainerViews(element: Element?, view: ModelView?) =
        element is SoftwareSystem && workspace.includedSoftwareSystems.contains(element) && element == view?.softwareSystem && element.hasContainers

    private fun getUrlToContainerViews(element: Element?): String {
        val path = "/${element?.name?.normalize()}/container/".asUrlToDirectory(url)
        return "$TEMP_URI$path"
    }

    private fun needsLinkToComponentViews(element: Element?, view: ModelView?) =
        element is Container && element.hasComponents && view !is ComponentView

    private fun getUrlToComponentViews(element: Element?): String {
        val path = "/${element?.parent?.name?.normalize()}/component/".asUrlToDirectory(url)
        return "$TEMP_URI$path"
    }

    private fun writeElementWithCustomUrl(element: Element?, url: String?, view: ModelView?, writer: IndentingWriter?) {
        element?.url = url
        writeModifiedElement(view, element, writer)
        element?.url = null
    }

    private fun writeModifiedElement(
        view: ModelView?,
        element: Element?,
        writer: IndentingWriter?
    ) = IndentingWriter().let {
        super.writeElement(view, element, it)
        it.toString()
            .replace(TEMP_URI, "")
            .split(System.lineSeparator())
            .forEach { line -> writer?.writeLine(line) }
    }
}
