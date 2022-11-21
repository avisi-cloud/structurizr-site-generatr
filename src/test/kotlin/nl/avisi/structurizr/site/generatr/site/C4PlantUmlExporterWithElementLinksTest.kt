package nl.avisi.structurizr.site.generatr.site

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isEqualTo
import com.structurizr.Workspace
import com.structurizr.view.SystemContextView
import kotlin.test.BeforeTest
import kotlin.test.Test

class C4PlantUmlExporterWithElementLinksTest {
    private lateinit var workspace: Workspace

    @BeforeTest
    fun setUp() {
        workspace = Workspace("workspace name", "")
    }

    @Test
    fun `adds skinparam to remove explicit size from generated svg`() {
        val view = createOneSystemWithSystemContextView()

        val diagram = C4PlantUmlExporterWithElementLinks(workspace, "/landscape/")
            .export(view)

        assertThat(diagram.definition)
            .contains("skinparam svgDimensionStyle false")
    }

    @Test
    fun `adds skinparam to preserve the aspect ratio of the generated svg`() {
        val view = createOneSystemWithSystemContextView()

        val diagram = C4PlantUmlExporterWithElementLinks(workspace, "/landscape/")
            .export(view)

        assertThat(diagram.definition)
            .contains("skinparam preserveAspectRatio meet")
    }

    @Test
    fun `renders diagram`() {
        val view = createOneSystemWithSystemContextView()

        val diagram = C4PlantUmlExporterWithElementLinks(workspace, "/landscape/")
            .export(view)

        assertThat(diagram.definition.withoutHeaderAndFooter()).isEqualTo(
            """
            System(System1, "System 1", "", ${'$'}tags="")
            """.trimIndent()
        )
    }

    @Test
    fun `link to other software system`() {
        val view = createThreeSystemsAndTwoSystemContextViews()

        val diagram = C4PlantUmlExporterWithElementLinks(workspace, "/landscape/")
            .export(view)

        assertThat(diagram.definition.withoutHeaderAndFooter()).isEqualTo(
            """
            System(System1, "System 1", "", ${'$'}tags="")
            System(System2, "System 2", "", ${'$'}tags="")[[../system-2/context]]
            System(System3, "System 3", "", ${'$'}tags="")[[../system-3]]

            Rel_D(System2, System1, "uses", ${'$'}tags="")
            Rel_D(System3, System1, "uses", ${'$'}tags="")
            """.trimIndent()
        )
    }

    @Test
    fun `link to other software system from two path segments deep`() {
        val view = createThreeSystemsAndTwoSystemContextViews()

        val diagram = C4PlantUmlExporterWithElementLinks(workspace, "/system-1/context/")
            .export(view)

        assertThat(diagram.definition.withoutHeaderAndFooter()).isEqualTo(
            """
            System(System1, "System 1", "", ${'$'}tags="")
            System(System2, "System 2", "", ${'$'}tags="")[[../../system-2/context]]
            System(System3, "System 3", "", ${'$'}tags="")[[../../system-3]]

            Rel_D(System2, System1, "uses", ${'$'}tags="")
            Rel_D(System3, System1, "uses", ${'$'}tags="")
            """.trimIndent()
        )
    }

    private fun createOneSystemWithSystemContextView(): SystemContextView {
        val system = workspace.model.addSoftwareSystem("System 1")

        return workspace.views.createSystemContextView(system, "Context1", "")
            .apply { addAllElements() }
    }

    private fun createThreeSystemsAndTwoSystemContextViews(): SystemContextView {
        val system1 = workspace.model.addSoftwareSystem("System 1")
        val system2 = workspace.model.addSoftwareSystem("System 2").apply { uses(system1, "uses") }

        workspace.model.addSoftwareSystem("System 3").apply { uses(system1, "uses") }

        workspace.views.createSystemContextView(system2, "Context 2", "")
            .apply { addAllElements() }

        return workspace.views.createSystemContextView(system1, "Context 1", "")
            .apply { addAllElements() }
    }

    private fun String.withoutHeaderAndFooter() = this
        .split(System.lineSeparator())
        .drop(10)
        .dropLast(3)
        .joinToString(System.lineSeparator())
        .trimEnd()
}
