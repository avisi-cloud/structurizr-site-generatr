package nl.avisi.structurizr.site.generatr.site

import com.structurizr.Workspace
import nl.avisi.structurizr.site.generatr.includedProperties
import kotlin.test.Test
import kotlin.test.assertEquals

class StructurizrUtilitiesTest {

    protected val svgFactory = { _: String, _: String -> "" }

    protected fun generatorContext(
        workspaceName: String = "Workspace name",
        branches: List<String> = listOf("main"),
        currentBranch: String = "main",
        version: String = "1.0.0"
    ) = GeneratorContext(version, Workspace(workspaceName, ""), branches, currentBranch, false, svgFactory)

    @Test
    fun `includedProperties filters out structurizr and generatr properties`() {
        val element = generatorContext().workspace.model.addSoftwareSystem("System 1")
        element.addProperty("structurizr.key", "value")
        element.addProperty("structurizrnotquite.key", "value")
        element.addProperty("generatrbut.not.key", "value")
        element.addProperty("other.key", "value")

        val includedProperties = element.includedProperties

        assertEquals(3, includedProperties.size)
        assertEquals(includedProperties.keys, setOf("structurizrnotquite.key", "generatrbut.not.key", "other.key"))
    }

    @Test
    fun `includedProperties returns all properties when none are filtered out`() {
        val element = generatorContext().workspace.model.addSoftwareSystem("System 1")
        element.addProperty("key1", "value1")
        element.addProperty("key2", "value2")

        val includedProperties = element.includedProperties

        assertEquals(2, includedProperties.size)
        assertEquals("key1", includedProperties.keys.first())
        assertEquals("key2", includedProperties.keys.last())
    }

    @Test
    fun `includedProperties returns empty map when no properties are set`() {
        val element = generatorContext().workspace.model.addSoftwareSystem("System 1")
        val includedProperties = element.includedProperties
        assertEquals(0, includedProperties.size)
    }
}
