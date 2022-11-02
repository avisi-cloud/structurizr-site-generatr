package nl.avisi.structurizr.site.generatr.site

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.structurizr.Workspace
import com.structurizr.model.SoftwareSystem
import org.junit.jupiter.api.Test

class C4PlantUmlExporterWithElementLinksTest {

    @Test
    fun `renders diagram`() {
        val (workspace, system) = createWorkspace()
        val view = workspace.views.createSystemContextView(system, "Context1", "")
            .apply { addAllElements() }

        val diagram = C4PlantUmlExporterWithElementLinks(workspace, "master")
            .export(view)

        assertThat(diagram.definition.withoutHeaderAndFooter()).isEqualTo(
            """
            System(System1, "System 1", "", ${'$'}tags="")
            """.withoutTrailingSpaces()
        )
    }

    @Test
    fun `link to other software system`() {
        val (workspace, system) = createWorkspace()
        workspace.model.addSoftwareSystem("System 2").apply { uses(system, "uses") }
        val view = workspace.views.createSystemContextView(system, "Context 1", "")
            .apply { addAllElements() }

        val diagram = C4PlantUmlExporterWithElementLinks(workspace, "master")
            .export(view)

        assertThat(diagram.definition.withoutHeaderAndFooter()).isEqualTo(
            """
            System(System1, "System 1", "", ${'$'}tags="")
            System(System2, "System 2", "", ${'$'}tags="")[[/master/system-2/context/]]

            Rel_D(System2, System1, "uses", ${'$'}tags="")
            """.withoutTrailingSpaces()
        )
    }

    private fun createWorkspace(): Pair<Workspace, SoftwareSystem> {
        val workspace = Workspace("workspace name", "")
        val system = workspace.model.addSoftwareSystem("System 1")
        return workspace to system
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
