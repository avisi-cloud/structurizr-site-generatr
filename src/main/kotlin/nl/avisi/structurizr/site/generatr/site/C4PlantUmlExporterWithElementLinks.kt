package nl.avisi.structurizr.site.generatr.site

import com.structurizr.Workspace
import com.structurizr.export.Diagram
import com.structurizr.export.IndentingWriter
import com.structurizr.export.plantuml.C4PlantUMLExporter
import com.structurizr.model.Component
import com.structurizr.model.Container
import com.structurizr.model.Element
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.*
import nl.avisi.structurizr.site.generatr.*

class C4PlantUmlExporterWithElementLinks(
    private val url: String,
    private val workspace: Workspace
): C4PlantUMLExporter() {
    companion object {
        const val TEMP_URI = "https://will-be-changed-to-relative/"

        fun C4PlantUMLExporter.export(view: View): Diagram = when (view) {
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
            needsLinkToSoftwareSystem(element, view) -> getUrlToViewsPage(element?.name?.normalize(), "context")
            needsLinkToContainerViews(element, view, workspace) -> getUrlToViewsPage(element?.name?.normalize(), "container")
            needsLinkToComponentViews(element, view, workspace) -> getUrlToViewsPage(element?.parent?.name?.normalize(), "component")
            needsLinkToCodeViews(element, workspace) -> getUrlToViewsPage(element?.parent?.parent?.name?.normalize(), "code")
            else -> null
        }

        if (url != null)
            writeElementWithCustomUrl(element, url, view, writer)
        else
            super.writeElement(view, element, writer)
    }

    private fun needsLinkToSoftwareSystem(element: Element?, view: ModelView?) =
        element is SoftwareSystem && element.includedSoftwareSystem && element != view?.softwareSystem

    private fun needsLinkToContainerViews(element: Element?, view: ModelView?, workspace: Workspace) =
        element is SoftwareSystem
                && element.includedSoftwareSystem
                && element == view?.softwareSystem
                && ( element.hasContainers || getImageViewsForId(workspace, element.id).isNotEmpty())

    private fun needsLinkToComponentViews(element: Element?, view: ModelView?, workspace: Workspace) =
        element is Container
                && ( element.hasComponents || getImageViewsForId(workspace, element.id).isNotEmpty())
                && view !is ComponentView

    private fun needsLinkToCodeViews(element: Element?, workspace: Workspace) =
        element is Component
                && getImageViewsForId(workspace, element.id).isNotEmpty()


    private fun getUrlToViewsPage(systemName: String?, page: String): String {
        val path = "/$systemName/$page/".asUrlToDirectory(url)
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
