package nl.avisi.structurizr.site.generatr.site

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isEqualTo
import com.structurizr.Workspace
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import kotlin.test.Test

class PlantUmlExporterTest {
    @TestFactory
    fun `adds skinparam to remove explicit size from generated svg`() = exporters().map { (type, exporterFn) ->
        DynamicTest.dynamicTest("($type)") {
            val workspace = createWorkspaceWithOneSystem()

            val diagram = exporterFn(workspace, "/landscape/").export(workspace.views.systemContextViews.first())

            assertThat(diagram.definition)
                .contains("skinparam svgDimensionStyle false")
        }
    }

    @TestFactory
    fun `adds skinparam to preserve the aspect ratio of the generated svg`() = exporters().map { (type, exporterFn) ->
        DynamicTest.dynamicTest("($type)") {
            val workspace = createWorkspaceWithOneSystem()

            val diagram = exporterFn(workspace, "/landscape/")
                .export(workspace.views.systemContextViews.first())

            assertThat(diagram.definition)
                .contains("skinparam preserveAspectRatio meet")
        }
    }

    private fun exporters() = listOf(
        ExporterType.C4.name.lowercase() to
            { workspace: Workspace, url: String -> C4PlantUmlExporterWithElementLinks(workspace, url) },
        ExporterType.STRUCTURIZR.name.lowercase() to
            { workspace: Workspace, url: String -> StructurizrPlantUmlExporterWithElementLinks(workspace, url) }
    )

    @Test
    fun `renders diagram (c4)`() {
        val workspace = createWorkspaceWithOneSystem()

        val diagram = C4PlantUmlExporterWithElementLinks(workspace, "/landscape/")
            .export(workspace.views.systemContextViews.first())

        assertThat(diagram.definition.withoutC4HeaderAndFooter()).isEqualTo(
            """
            System(System1, "System 1", ${'$'}descr="", ${'$'}tags="", ${'$'}link="")
            """.trimIndent()
        )
    }

    @Test
    fun `renders diagram (structurizr)`() {
        val workspace = createWorkspaceWithOneSystem()

        val diagram = StructurizrPlantUmlExporterWithElementLinks(workspace, "/landscape/")
            .export(workspace.views.systemContextViews.first())

        assertThat(diagram.definition.withoutStructurizrHeaderAndFooter()).isEqualTo(
            """
            rectangle "==System 1\n<size:10>[Software System]</size>" <<System1>> as System1
            """.trimIndent()
        )
    }

    @Test
    fun `renders System Diagram with link to container (c4)`() {
        val workspace = createWorkspaceWithOneSystemWithContainers()

        val diagram = C4PlantUmlExporterWithElementLinks(workspace, "/container/")
            .export(workspace.views.systemContextViews.first())

        assertThat(diagram.definition.withoutC4HeaderAndFooter()).isEqualTo(
            """
            System(System1, "System 1", ${'$'}descr="", ${'$'}tags="", ${'$'}link="../system-1/container/")
            """.trimIndent()
        )
    }

    @Test
    fun `renders System Diagram with link to container (structurizr)`() {
        val workspace = createWorkspaceWithOneSystemWithContainers()

        val diagram = StructurizrPlantUmlExporterWithElementLinks(workspace, "/container/")
            .export(workspace.views.systemContextViews.first())

        assertThat(diagram.definition.withoutStructurizrHeaderAndFooter()).isEqualTo(
            """
            rectangle "==System 1\n<size:10>[Software System]</size>" <<System1>> as System1 [[../system-1/container/]]
            """.trimIndent()
        )
    }

    @Test
    fun `renders System Diagram with link to other system and link to container (c4)`() {
        val workspace = createSystemContextViewForWorkspaceWithTwoSystemWithContainers()

        val diagram = C4PlantUmlExporterWithElementLinks(workspace, "/container/")
            .export(workspace.views.systemContextViews.first())

        assertThat(diagram.definition.withoutC4HeaderAndFooter()).isEqualTo(
            """
            System(System1, "System 1", ${'$'}descr="", ${'$'}tags="", ${'$'}link="../system-1/container/")
            System(System2, "System 2", ${'$'}descr="", ${'$'}tags="", ${'$'}link="../system-2/context/")

            Rel(System2, System1, "uses", ${'$'}techn="", ${'$'}tags="", ${'$'}link="")
            """.trimIndent()
        )
    }

    @Test
    fun `renders System Diagram with link to other system and link to container (structurizr)`() {
        val workspace = createSystemContextViewForWorkspaceWithTwoSystemWithContainers()

        val diagram = StructurizrPlantUmlExporterWithElementLinks(workspace, "/container/")
            .export(workspace.views.systemContextViews.first())

        assertThat(diagram.definition.withoutStructurizrHeaderAndFooter()).isEqualTo(
            """
            rectangle "==System 1\n<size:10>[Software System]</size>" <<System1>> as System1 [[../system-1/container/]]
            rectangle "==System 2\n<size:10>[Software System]</size>" <<System2>> as System2 [[../system-2/context/]]

            System2 .[#707070,thickness=2].> System1 : "<color:#707070>uses"
            """.trimIndent()
        )
    }

    @Test
    fun `renders Container Diagram with link to component diagram (c4)`() {
        val workspace = createWorkspaceWithOneSystemWithContainersAndComponents()

        val diagram = C4PlantUmlExporterWithElementLinks(workspace, "/container/")
            .export(workspace.views.containerViews.first())

        assertThat(diagram.definition.withoutC4HeaderAndFooter()).isEqualTo(
            """
            System_Boundary("System1_boundary", "System 1", ${'$'}tags="") {
              Container(System1.Container1, "Container 1", ${'$'}techn="", ${'$'}descr="", ${'$'}tags="", ${'$'}link="../system-1/component/container-1/")
            }
            """.trimIndent()
        )
    }

    @Test
    fun `renders Container Diagram with link to component diagram (structurizr)`() {
        val workspace = createWorkspaceWithOneSystemWithContainersAndComponents()

        val diagram = StructurizrPlantUmlExporterWithElementLinks(workspace, "/container/")
            .export(workspace.views.containerViews.first())

        assertThat(diagram.definition.withoutStructurizrHeaderAndFooter()).isEqualTo(
            """
            rectangle "System 1\n<size:10>[Software System]</size>" <<System1>> {
              rectangle "==Container 1\n<size:10>[Container]</size>" <<System1.Container1>> as System1.Container1 [[../system-1/component/container-1/]]
            }
            """.trimIndent()
        )
    }

    @Test
    fun `renders Container Diagram with link to code view (c4)`() {
        val workspace = createWorkspaceWithOneSystemWithContainersComponentAndImage()

        val diagram = C4PlantUmlExporterWithElementLinks(workspace, "/container/")
            .export(workspace.views.componentViews.first())

        assertThat(diagram.definition.withoutC4HeaderAndFooter()).isEqualTo(
            """
            Container_Boundary("System1.Container1_boundary", "Container 1", ${'$'}tags="") {
              Component(System1.Container1.Component1, "Component 1", ${'$'}techn="", ${'$'}descr="", ${'$'}tags="", ${'$'}link="../system-1/code/container-1/component-1/")
              Component(System1.Container1.Component2, "Component 2", ${'$'}techn="", ${'$'}descr="", ${'$'}tags="", ${'$'}link="")
              Component(System1.Container1.Component3, "Component 3", ${'$'}techn="", ${'$'}descr="", ${'$'}tags="", ${'$'}link="")
            }
            """.trimIndent()
        )
    }

    @Test
    fun `renders Container Diagram with link to code view (structurizr)`() {
        val workspace = createWorkspaceWithOneSystemWithContainersComponentAndImage()

        val diagram = StructurizrPlantUmlExporterWithElementLinks(workspace, "/container/")
            .export(workspace.views.componentViews.first())

        assertThat(diagram.definition.withoutStructurizrHeaderAndFooter()).isEqualTo(
            """
            rectangle "Container 1\n<size:10>[Container]</size>" <<System1.Container1>> {
              rectangle "==Component 1\n<size:10>[Component]</size>" <<System1.Container1.Component1>> as System1.Container1.Component1 [[../system-1/code/container-1/component-1/]]
              rectangle "==Component 2\n<size:10>[Component]</size>" <<System1.Container1.Component2>> as System1.Container1.Component2
              rectangle "==Component 3\n<size:10>[Component]</size>" <<System1.Container1.Component3>> as System1.Container1.Component3
            }
            """.trimIndent()
        )
    }

    @Test
    fun `renders Components Diagram without link to other Component Diagram (c4)`() {
        val workspace = Workspace("workspace name", "")
        val system = workspace.model.addSoftwareSystem("System 1")
        val container1 = system.addContainer("Container 1")
        val container2 = system.addContainer("Container 2")
        container1.addComponent("Component 1").apply { uses(container2, "uses") }
        container2.addComponent("Component 2")

        val view = workspace.views.createComponentView(container1, "Component2", "")
            .apply { addAllElements() }

        val diagram = C4PlantUmlExporterWithElementLinks(workspace, "/system-1/component/")
            .export(view)

        assertThat(diagram.definition.withoutC4HeaderAndFooter()).isEqualTo(
            """
            Container(System1.Container2, "Container 2", ${'$'}techn="", ${'$'}descr="", ${'$'}tags="", ${'$'}link="")

            Container_Boundary("System1.Container1_boundary", "Container 1", ${'$'}tags="") {
              Component(System1.Container1.Component1, "Component 1", ${'$'}techn="", ${'$'}descr="", ${'$'}tags="", ${'$'}link="")
            }

            Rel(System1.Container1.Component1, System1.Container2, "uses", ${'$'}techn="", ${'$'}tags="", ${'$'}link="")
            """.trimIndent()
        )
    }

    @Test
    fun `renders Components Diagram without link to other Component Diagram (structurizr)`() {
        val workspace = Workspace("workspace name", "")
        val system = workspace.model.addSoftwareSystem("System 1")
        val container1 = system.addContainer("Container 1")
        val container2 = system.addContainer("Container 2")
        container1.addComponent("Component 1").apply { uses(container2, "uses") }
        container2.addComponent("Component 2")

        val view = workspace.views.createComponentView(container1, "Component2", "")
            .apply { addAllElements() }

        val diagram = StructurizrPlantUmlExporterWithElementLinks(workspace, "/system-1/component/")
            .export(view)

        assertThat(diagram.definition.withoutStructurizrHeaderAndFooter()).isEqualTo(
            """
            rectangle "==Container 2\n<size:10>[Container]</size>" <<System1.Container2>> as System1.Container2

            rectangle "Container 1\n<size:10>[Container]</size>" <<System1.Container1>> {
              rectangle "==Component 1\n<size:10>[Component]</size>" <<System1.Container1.Component1>> as System1.Container1.Component1
            }

            System1.Container1.Component1 .[#707070,thickness=2].> System1.Container2 : "<color:#707070>uses"
            """.trimIndent()
        )
    }

    @Test
    fun `link to other software system (c4)`() {
        val workspace = createWorkspaceWithTwoSystems()

        val diagram = C4PlantUmlExporterWithElementLinks(workspace, "/landscape/")
            .export(workspace.views.systemContextViews.first())

        assertThat(diagram.definition.withoutC4HeaderAndFooter()).isEqualTo(
            """
            System(System1, "System 1", ${'$'}descr="", ${'$'}tags="", ${'$'}link="")
            System(System2, "System 2", ${'$'}descr="", ${'$'}tags="", ${'$'}link="../system-2/context/")

            Rel(System2, System1, "uses", ${'$'}techn="", ${'$'}tags="", ${'$'}link="")
            """.trimIndent()
        )
    }

    @Test
    fun `link to other software system (structurizr)`() {
        val workspace = createWorkspaceWithTwoSystems()

        val diagram = StructurizrPlantUmlExporterWithElementLinks(workspace, "/landscape/")
            .export(workspace.views.systemContextViews.first())

        assertThat(diagram.definition.withoutStructurizrHeaderAndFooter()).isEqualTo(
            """
            rectangle "==System 1\n<size:10>[Software System]</size>" <<System1>> as System1
            rectangle "==System 2\n<size:10>[Software System]</size>" <<System2>> as System2 [[../system-2/context/]]

            System2 .[#707070,thickness=2].> System1 : "<color:#707070>uses"
            """.trimIndent()
        )
    }

    @Test
    fun `external software system (declared external by tag) (c4)`() {
        val workspace = createWorkspaceWithTwoSystems()
        workspace.views.configuration.addProperty("generatr.site.externalTag", "External System")
        workspace.model.softwareSystems.single { it.name == "System 2" }.addTags("External System")
        val view = workspace.views.systemContextViews.first()

        val diagram = C4PlantUmlExporterWithElementLinks(workspace, "/landscape/")
            .export(view)

        assertThat(diagram.definition.withoutC4HeaderAndFooter()).isEqualTo(
            """
            System(System1, "System 1", ${'$'}descr="", ${'$'}tags="", ${'$'}link="")
            System(System2, "System 2", ${'$'}descr="", ${'$'}tags="", ${'$'}link="")

            Rel(System2, System1, "uses", ${'$'}techn="", ${'$'}tags="", ${'$'}link="")
            """.trimIndent()
        )
    }

    @Test
    fun `external software system (declared external by tag) (structurizr)`() {
        val workspace = createWorkspaceWithTwoSystems()
        workspace.views.configuration.addProperty("generatr.site.externalTag", "External System")
        workspace.model.softwareSystems.single { it.name == "System 2" }.addTags("External System")
        val view = workspace.views.systemContextViews.first()

        val diagram = StructurizrPlantUmlExporterWithElementLinks(workspace, "/landscape/")
            .export(view)

        assertThat(diagram.definition.withoutStructurizrHeaderAndFooter()).isEqualTo(
            """
            rectangle "==System 1\n<size:10>[Software System]</size>" <<System1>> as System1
            rectangle "==System 2\n<size:10>[Software System]</size>" <<System2>> as System2

            System2 .[#707070,thickness=2].> System1 : "<color:#707070>uses"
            """.trimIndent()
        )
    }

    @Test
    fun `link to other software system from two path segments deep (c4)`() {
        val workspace = createWorkspaceWithTwoSystems()

        val diagram = C4PlantUmlExporterWithElementLinks(workspace, "/system-1/context/")
            .export(workspace.views.systemContextViews.first())

        assertThat(diagram.definition.withoutC4HeaderAndFooter()).isEqualTo(
            """
            System(System1, "System 1", ${'$'}descr="", ${'$'}tags="", ${'$'}link="")
            System(System2, "System 2", ${'$'}descr="", ${'$'}tags="", ${'$'}link="../../system-2/context/")

            Rel(System2, System1, "uses", ${'$'}techn="", ${'$'}tags="", ${'$'}link="")
            """.trimIndent()
        )
    }

    @Test
    fun `link to other software system from two path segments deep (structurizr)`() {
        val workspace = createWorkspaceWithTwoSystems()

        val diagram = StructurizrPlantUmlExporterWithElementLinks(workspace, "/system-1/context/")
            .export(workspace.views.systemContextViews.first())

        assertThat(diagram.definition.withoutStructurizrHeaderAndFooter()).isEqualTo(
            """
            rectangle "==System 1\n<size:10>[Software System]</size>" <<System1>> as System1
            rectangle "==System 2\n<size:10>[Software System]</size>" <<System2>> as System2 [[../../system-2/context/]]

            System2 .[#707070,thickness=2].> System1 : "<color:#707070>uses"
            """.trimIndent()
        )
    }

    private fun createWorkspaceWithOneSystem(): Workspace {
        val workspace = Workspace("workspace name", "").apply {
            views.configuration.addProperty("generatr.site.excludedTag", "External System")
        }
        val system = workspace.model.addSoftwareSystem("System 1")

        workspace.views.createSystemContextView(system, "Context1", "")
            .apply { addAllElements() }

        return workspace
    }

    private fun createWorkspaceWithOneSystemWithContainers(): Workspace {
        val workspace = Workspace("workspace name", "")
        val system = workspace.model.addSoftwareSystem("System 1")
        system.addContainer("Container 1")
        system.addContainer("Container 2")

        workspace.views.createSystemContextView(system, "Context 1", "")
            .apply { addAllElements() }

        workspace.views.createContainerView(system, "Container1", "")
            .apply { addAllElements() }

        return workspace
    }

    private fun createSystemContextViewForWorkspaceWithTwoSystemWithContainers(): Workspace {
        val workspace = Workspace("workspace name", "")
        val system = workspace.model.addSoftwareSystem("System 1")
        workspace.model.addSoftwareSystem("System 2").apply { uses(system, "uses") }
        system.addContainer("Container 1")
        system.addContainer("Container 2")

        workspace.views.createSystemContextView(system, "Context 1", "")
            .apply { addAllElements() }

        workspace.views.createContainerView(system, "Container1", "")
            .apply { addAllElements() }

        return workspace
    }

    private fun createWorkspaceWithOneSystemWithContainersAndComponents(): Workspace {
        val workspace = Workspace("workspace name", "")
        val system = workspace.model.addSoftwareSystem("System 1")
        val container = system.addContainer("Container 1")
        container.addComponent("Component 1")
        container.addComponent("Component 2")
        container.addComponent("Component 3")

        workspace.views.createSystemContextView(system, "Context 1", "")
            .apply { addAllElements() }

        workspace.views.createContainerView(system, "Container1", "")
            .apply { addAllElements() }

        workspace.views.createComponentView(container, "Component1", "")
            .apply { addAllElements() }

        return workspace
    }

    private fun createWorkspaceWithOneSystemWithContainersComponentAndImage() = createWorkspaceWithOneSystemWithContainersAndComponents().apply {
        views.createImageView(model.softwareSystems.single().containers.single().components.single { it.name == "Component 1" }, "imageview-001").also {
            it.description = "Image View Description"
            it.title = "Image View Title"
            it.contentType = "image/png"
            it.content = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C8AAAAASUVORK5CYII="
        }
    }

    private fun createWorkspaceWithTwoSystems(): Workspace {
        val workspace = Workspace("workspace name", "")
        val system = workspace.model.addSoftwareSystem("System 1")
        workspace.model.addSoftwareSystem("System 2").apply { uses(system, "uses") }

        workspace.views.createSystemContextView(system, "Context 1", "")
            .apply { addAllElements() }

        return workspace
    }

    private fun String.withoutC4HeaderAndFooter() = this
        .split(System.lineSeparator())
        .dropWhile { !it.startsWith("System") && !it.startsWith("Container") }
        .dropLast(3)
        .joinToString(System.lineSeparator())
        .trimEnd()

    private fun String.withoutStructurizrHeaderAndFooter() = this
        .split(System.lineSeparator())
        .dropWhile { !it.startsWith("rectangle") }
        .dropLast(1)
        .joinToString(System.lineSeparator())
        .trimEnd()
}
