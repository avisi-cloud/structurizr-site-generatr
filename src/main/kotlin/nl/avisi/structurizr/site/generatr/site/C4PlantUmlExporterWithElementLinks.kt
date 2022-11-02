package nl.avisi.structurizr.site.generatr.site

import com.structurizr.Workspace
import com.structurizr.export.IndentingWriter
import com.structurizr.export.plantuml.C4PlantUMLExporter
import com.structurizr.model.Element
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.View
import nl.avisi.structurizr.site.generatr.includedSoftwareSystems
import nl.avisi.structurizr.site.generatr.normalize

class C4PlantUmlExporterWithElementLinks(
    private val workspace: Workspace,
    private val branch: String
): C4PlantUMLExporter() {
    companion object {
        const val TEMP_URI = "https://will-be-changed-to-relative"
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
        element.url = "${TEMP_URI}/$branch/${element.name.normalize()}/context/"
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
