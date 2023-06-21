package nl.avisi.structurizr.site.generatr.site

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isEqualTo
import com.structurizr.Workspace
import com.structurizr.view.ContainerView
import com.structurizr.view.SystemContextView
import kotlin.test.Test

class C4PlantUmlExporterWithElementLinksTest {
    @Test
    fun `adds skinparam to remove explicit size from generated svg`() {
        val view = createWorkspaceWithOneSystem()

        val diagram = C4PlantUmlExporterWithElementLinks("/landscape/")
            .export(view)

        assertThat(diagram.definition)
            .contains("skinparam svgDimensionStyle false")
    }

    @Test
    fun `adds skinparam to preserve the aspect ratio of the generated svg`() {
        val view = createWorkspaceWithOneSystem()

        val diagram = C4PlantUmlExporterWithElementLinks("/landscape/")
            .export(view)

        assertThat(diagram.definition)
            .contains("skinparam preserveAspectRatio meet")
    }

    @Test
    fun `renders diagram`() {
        val view = createWorkspaceWithOneSystem()

        val diagram = C4PlantUmlExporterWithElementLinks("/landscape/")
            .export(view)

        assertThat(diagram.definition.withoutHeaderAndFooter()).isEqualTo(
            """
            System(System1, "System 1", ${'$'}descr="", ${'$'}tags="", ${'$'}link="")
            """.trimIndent()
        )
    }

    @Test
    fun `renders System Diagram with link to container`() {
        val view = createWorkspaceWithOneSystemWithContainers()

        val diagram = C4PlantUmlExporterWithElementLinks("/container/")
            .export(view)

        assertThat(diagram.definition.withoutHeaderAndFooter()).isEqualTo(
            """
            System(System1, "System 1", ${'$'}descr="", ${'$'}tags="", ${'$'}link="../system-1/container/")
            """.trimIndent()
        )
    }

    @Test
    fun `renders System Diagram with link to other system and link to container`() {
        val view = createWorkspaceWithTwoSystemWithContainers()

        val diagram = C4PlantUmlExporterWithElementLinks("/container/")
            .export(view)

        assertThat(diagram.definition.withoutHeaderAndFooter()).isEqualTo(
            """
            System(System1, "System 1", ${'$'}descr="", ${'$'}tags="", ${'$'}link="../system-1/container/")
            System(System2, "System 2", ${'$'}descr="", ${'$'}tags="", ${'$'}link="../system-2/context/")

            Rel_D(System2, System1, "uses", ${'$'}techn="", ${'$'}tags="", ${'$'}link="")
            """.trimIndent()
        )
    }

    @Test
    fun `renders Container Diagram with link to component diagram`() {
        val view = createWorkspaceWithOneSystemWithContainersAndComponents()

        val diagram = C4PlantUmlExporterWithElementLinks("/container/")
            .export(view)

        assertThat(diagram.definition.withoutHeaderAndFooter()).isEqualTo(
            """
            skinparam preserveAspectRatio meet
            System_Boundary("System1_boundary", "System 1", ${'$'}tags="") {
              Container(System1.Container1, "Container 1", ${'$'}techn="", ${'$'}descr="", ${'$'}tags="", ${'$'}link="../system-1/component/")
            }
            """.trimIndent()
        )
    }

    @Test
    fun `renders Components Diagram without link to other Component Diagram`() {
        val workspace = Workspace("workspace name", "")
        val system = workspace.model.addSoftwareSystem("System 1")
        val container1 = system.addContainer("Container 1")
        val container2 = system.addContainer("Container 2")
        container1.addComponent("Component 1").apply { uses(container2, "uses") }
        container2.addComponent("Component 2")

        val view = workspace.views.createComponentView(container1, "Component2", "")
            .apply { addAllElements() }

        val diagram = C4PlantUmlExporterWithElementLinks("/system-1/component/")
            .export(view)

        assertThat(diagram.definition.withoutHeaderAndFooter()).isEqualTo(
            """
            skinparam svgDimensionStyle false
            skinparam preserveAspectRatio meet
            Container(System1.Container2, "Container 2", ${'$'}techn="", ${'$'}descr="", ${'$'}tags="", ${'$'}link="")

            Container_Boundary("System1.Container1_boundary", "Container 1", ${'$'}tags="") {
              Component(System1.Container1.Component1, "Component 1", ${'$'}techn="", ${'$'}descr="", ${'$'}tags="", ${'$'}link="")
            }

            Rel_D(System1.Container1.Component1, System1.Container2, "uses", ${'$'}techn="", ${'$'}tags="", ${'$'}link="")
            """.trimIndent()
        )
    }

    @Test
    fun `link to other software system`() {
        val view = createWorkspaceWithTwoSystems()

        val diagram = C4PlantUmlExporterWithElementLinks("/landscape/")
            .export(view)

        assertThat(diagram.definition.withoutHeaderAndFooter()).isEqualTo(
            """
            System(System1, "System 1", ${'$'}descr="", ${'$'}tags="", ${'$'}link="")
            System(System2, "System 2", ${'$'}descr="", ${'$'}tags="", ${'$'}link="../system-2/context/")

            Rel_D(System2, System1, "uses", ${'$'}techn="", ${'$'}tags="", ${'$'}link="")
            """.trimIndent()
        )
    }

    @Test
    fun `link to other software system from two path segments deep`() {
        val view = createWorkspaceWithTwoSystems()

        val diagram = C4PlantUmlExporterWithElementLinks("/system-1/context/")
            .export(view)

        assertThat(diagram.definition.withoutHeaderAndFooter()).isEqualTo(
            """
            System(System1, "System 1", ${'$'}descr="", ${'$'}tags="", ${'$'}link="")
            System(System2, "System 2", ${'$'}descr="", ${'$'}tags="", ${'$'}link="../../system-2/context/")

            Rel_D(System2, System1, "uses", ${'$'}techn="", ${'$'}tags="", ${'$'}link="")
            """.trimIndent()
        )
    }

    private fun createWorkspaceWithOneSystem(): SystemContextView {
        val workspace = Workspace("workspace name", "")
        val system = workspace.model.addSoftwareSystem("System 1")

        return workspace.views.createSystemContextView(system, "Context1", "")
            .apply { addAllElements() }
    }

    private fun createWorkspaceWithOneSystemWithContainers(): SystemContextView? {
        val workspace = Workspace("workspace name", "")
        val system = workspace.model.addSoftwareSystem("System 1")
        system.addContainer("Container 1")
        system.addContainer("Container 2")

        workspace.views.createContainerView(system, "Container1", "")
            .apply { addAllElements() }

        return workspace.views.createSystemContextView(system, "Context 1", "")
            .apply { addAllElements() }
    }

    private fun createWorkspaceWithTwoSystemWithContainers(): SystemContextView? {
        val workspace = Workspace("workspace name", "")
        val system = workspace.model.addSoftwareSystem("System 1")
        workspace.model.addSoftwareSystem("System 2").apply { uses(system, "uses") }
        system.addContainer("Container 1")
        system.addContainer("Container 2")

        workspace.views.createContainerView(system, "Container1", "")
            .apply { addAllElements() }

        return workspace.views.createSystemContextView(system, "Context 1", "")
            .apply { addAllElements() }
    }

    private fun createWorkspaceWithOneSystemWithContainersAndComponents(): ContainerView? {
        val workspace = Workspace("workspace name", "")
        val system = workspace.model.addSoftwareSystem("System 1")
        val container = system.addContainer("Container 1")
        container.addComponent("Component 1")
        container.addComponent("Component 2")
        container.addComponent("Component 3")

        workspace.views.createSystemContextView(system, "Context 1", "")
            .apply { addAllElements() }

        workspace.views.createComponentView(container, "Component1", "")

        return workspace.views.createContainerView(system, "Container1", "")
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
        .drop(11)
        .dropLast(3)
        .joinToString(System.lineSeparator())
        .trimEnd()
}
