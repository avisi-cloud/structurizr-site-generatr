package nl.avisi.structurizr.site.generatr

import com.structurizr.dsl.StructurizrDslParser
import com.structurizr.model.Element
import com.structurizr.view.ThemeUtils
import java.io.File

/**
 * The url an element defines in the model is moved here, because the url field itself is
 * used to carry the generated drill-down links. Read it from this property to render the
 * element's own link.
 */
const val ORIGINAL_URL_PROPERTY = "Url"

fun createStructurizrWorkspace(workspaceFile: File) =
    StructurizrDslParser()
        .apply { parse(workspaceFile) }
        .workspace
        .apply {
            ThemeUtils.loadThemes(this)
            model.elements.forEach {
                moveUrlToProperty(it) // We need the URL later for our own links, preserve the original in a property
            }
        }
        ?: throw IllegalStateException("Workspace could not be parsed")

private fun moveUrlToProperty(element: Element) {
    if (element.url != null) {
        element.addProperty(ORIGINAL_URL_PROPERTY, element.url)
        element.url = null
    }
}
