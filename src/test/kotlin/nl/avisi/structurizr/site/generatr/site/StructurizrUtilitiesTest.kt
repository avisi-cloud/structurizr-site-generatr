package nl.avisi.structurizr.site.generatr.site

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.structurizr.Workspace
import nl.avisi.structurizr.site.generatr.includedProperties
import kotlin.test.Test

class StructurizrUtilitiesTest {

    private val svgFactory = { _: String, _: String -> "" to "" }

    private fun generatorContext(
        workspaceName: String = "Workspace name",
        branches: List<String> = listOf("main"),
        currentBranch: String = "main",
        version: String = "1.0.0"
    ) = GeneratorContext(version, Workspace(workspaceName, ""), branches, currentBranch, false, svgFactory)

    @Test
    fun `includedProperties filters out structurizr and generatr properties`() {
        val element = generatorContext().workspace.model.addSoftwareSystem("System 1")
        element.addProperty("structurizr.key", "value")
        element.addProperty("generatr.key", "value")
        element.addProperty("structurizrnotquite.key", "value")
        element.addProperty("generatrbut.not.key", "value")
        element.addProperty("other.key", "value")

        val includedProperties = element.includedProperties

        assertThat(includedProperties.size).isEqualTo(3)
        assertThat(includedProperties.keys).isEqualTo(setOf("structurizrnotquite.key", "generatrbut.not.key", "other.key"))
    }

    @Test
    fun `includedProperties returns all properties when none are filtered out`() {
        val element = generatorContext().workspace.model.addSoftwareSystem("System 1")
        element.addProperty("key1", "value1")
        element.addProperty("key2", "value2")

        val includedProperties = element.includedProperties

        assertThat(includedProperties.size).isEqualTo(2)
        assertThat(includedProperties.keys.first()).isEqualTo("key1")
        assertThat(includedProperties.keys.last()).isEqualTo("key2")
    }

    @Test
    fun `includedProperties returns empty map when no properties are set`() {
        val element = generatorContext().workspace.model.addSoftwareSystem("System 1")
        val includedProperties = element.includedProperties
        assertThat(includedProperties.size).isEqualTo(0)
    }
}
