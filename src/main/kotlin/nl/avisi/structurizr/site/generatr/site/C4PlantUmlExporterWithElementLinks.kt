package nl.avisi.structurizr.site.generatr.site

import com.structurizr.export.Diagram
import com.structurizr.export.IndentingWriter
import com.structurizr.export.plantuml.C4PlantUMLExporter
import com.structurizr.model.Container
import com.structurizr.model.Element
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.*
import nl.avisi.structurizr.site.generatr.*

class C4PlantUmlExporterWithElementLinks(
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

    override fun writeHeader(view: ModelView, writer: IndentingWriter) {
        super.writeHeader(view, writer)
        writer.writeLine("skinparam svgDimensionStyle false")
        writer.writeLine("skinparam preserveAspectRatio meet")
    }

    override fun writeElement(view: ModelView?, element: Element?, writer: IndentingWriter?) {
        if (element is SoftwareSystem) {
            if (element.includedSoftwareSystem && element != view?.softwareSystem) {
                setSoftwareSystemUrl(element)
                writeModifiedElement(view, element, writer)
                restoreElement(element)
                return
            } else if (element.includedSoftwareSystem && element == view?.softwareSystem){
                if (element.hasContainers) {
                    setContainerUrl(element)
                    writeModifiedElement(view, element, writer)
                    restoreElement(element)
                    return
                } else {
                    return super.writeElement(view, element, writer)
                }
            } else {
                return super.writeElement(view, element, writer)
            }
        }

        if (element is Container) {
            if (element.hasComponents) {
                setComponentUrl(element)
                writeModifiedElement(view, element, writer)
                restoreElement(element)
                return
            } else {
                return super.writeElement(view, element, writer)
            }
        }

        return super.writeElement(view, element, writer)
    }

    private fun setSoftwareSystemUrl(element: Element) {
        val path = "/${element.name.normalize()}/context/".asUrlToDirectory(url)
        element.url = "${TEMP_URI}$path"
    }

    private fun setContainerUrl(element: Element) {
        val path = "/${element.name.normalize()}/container/".asUrlToDirectory(url)
        element.url = "${TEMP_URI}$path"
    }

    private fun setComponentUrl(element: Element) {
        val anchor = element.canonicalName.normalize()
            .replace("container://","")
            .replace("-","")
            .replace(".","-") + "-component"
        val path = "/${element.parent.name.normalize()}/component/#${anchor}".asUrlToFile(url)
        element.url = "${TEMP_URI}$path"
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

    private fun restoreElement(element: Element) {
        element.url = null
    }
}
