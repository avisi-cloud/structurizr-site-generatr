package nl.avisi.structurizr.site.generatr.site

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.structurizr.Workspace
import com.structurizr.view.SystemContextView
import org.junit.jupiter.api.Test

class C4PlantUmlExporterWithElementLinksTest {

    @Test
    fun `renders diagram`() {
        val view = createWorkspaceWithOneSystem()

        val diagram = C4PlantUmlExporterWithElementLinks("/landscape/")
            .export(view)

        assertThat(diagram.definition.withoutHeaderAndFooter()).isEqualTo(
            """
            System(System1, "System 1", "", ${'$'}tags="")
            """.withoutTrailingSpaces()
        )
    }

    @Test
    fun `link to other software system`() {
        val view = createWorkspaceWithTwoSystems()

        val diagram = C4PlantUmlExporterWithElementLinks("/landscape/")
            .export(view)

        assertThat(diagram.definition.withoutHeaderAndFooter()).isEqualTo(
            """
            System(System1, "System 1", "", ${'$'}tags="")
            System(System2, "System 2", "", ${'$'}tags="")[[../system-2/context]]

            Rel_D(System2, System1, "uses", ${'$'}tags="")
            """.withoutTrailingSpaces()
        )
    }

    @Test
    fun `link to other software system from two path segments deep`() {
        val view = createWorkspaceWithTwoSystems()

        val diagram = C4PlantUmlExporterWithElementLinks("/system-1/context/")
            .export(view)

        assertThat(diagram.definition.withoutHeaderAndFooter()).isEqualTo(
            """
            System(System1, "System 1", "", ${'$'}tags="")
            System(System2, "System 2", "", ${'$'}tags="")[[../../system-2/context]]

            Rel_D(System2, System1, "uses", ${'$'}tags="")
            """.withoutTrailingSpaces()
        )
    }

    private fun createWorkspaceWithOneSystem(): SystemContextView {
        val workspace = Workspace("workspace name", "")
        val system = workspace.model.addSoftwareSystem("System 1")

        return workspace.views.createSystemContextView(system, "Context1", "")
            .apply { addAllElements() }
    }

    private fun createWorkspaceWithTwoSystems(): SystemContextView {
        val workspace = Workspace("workspace name", "")
        val system = workspace.model.addSoftwareSystem("System 1")
        workspace.model.addSoftwareSystem("System 2").apply { uses(system, "uses") }

        return workspace.views.createSystemContextView(system, "Context 1", "")
            .apply { addAllElements() }
    }

    private fun String.withoutHeaderAndFooter() = this
        .split(System.lineSeparator())
        .drop(7)
        .dropLast(3)
        .joinToString(System.lineSeparator())
        .trimEnd()

    private fun String.withoutTrailingSpaces() = this
        .split(System.lineSeparator())
        .joinToString(System.lineSeparator()) { it.trim() }
        .trimEnd()
}
