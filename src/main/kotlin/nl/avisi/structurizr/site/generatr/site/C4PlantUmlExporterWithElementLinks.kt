package nl.avisi.structurizr.site.generatr.site

import com.structurizr.Workspace
import com.structurizr.export.Diagram
import com.structurizr.export.IndentingWriter
import com.structurizr.export.plantuml.C4PlantUMLExporter
import com.structurizr.model.Element
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.ComponentView
import com.structurizr.view.ContainerView
import com.structurizr.view.CustomView
import com.structurizr.view.DeploymentView
import com.structurizr.view.DynamicView
import com.structurizr.view.SystemContextView
import com.structurizr.view.SystemLandscapeView
import com.structurizr.view.View
import nl.avisi.structurizr.site.generatr.includedSoftwareSystems
import nl.avisi.structurizr.site.generatr.normalize

class C4PlantUmlExporterWithElementLinks(
    private val workspace: Workspace,
    private val url: String
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

    override fun writeElement(view: View?, element: Element?, writer: IndentingWriter?) {
        if (element !is SoftwareSystem || !element.linkNeeded(view))
            return super.writeElement(view, element, writer)

        setElementUrl(element)
        writeModifiedElement(view, element, writer)
        restoreElement(element)
    }

    private fun Element.linkNeeded(view: View?) =
        workspace.model.includedSoftwareSystems.contains(this) && this != view?.softwareSystem

    private fun setElementUrl(element: Element) {
        val path = "/${element.name.normalize()}/context/".asUrlRelativeTo(url)
        element.url = "${TEMP_URI}$path"
    }

    private fun writeModifiedElement(
        view: View?,
        element: Element?,
        writer: IndentingWriter?
    ) = IndentingWriter().let {
        super.writeElement(view, element, it)
        it.toString()
            .replace(TEMP_URI, "")
            .split(System.lineSeparator())
            .forEach { line -> writer?.writeLine(line) }
    }

    private fun restoreElement(element: Element) {
        element.url = null
    }
}
