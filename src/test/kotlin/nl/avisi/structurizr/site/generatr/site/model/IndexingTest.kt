package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.containsAll
import assertk.assertions.containsAtLeast
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import com.structurizr.documentation.Format
import com.structurizr.documentation.Section
import nl.avisi.structurizr.site.generatr.site.model.indexing.*
import kotlin.test.Test

class IndexingTest : ViewModelTest() {
    val workspace = this.generatorContext().workspace

    @Test
    fun `no home`() {
        val document = home(workspace.documentation, this.pageViewModel())

        assertThat(document).isNull()
    }

    @Test
    fun `indexes home`() {
        workspace.documentation.addSection(createSection("# Home\nSome info"))
        val document = home(workspace.documentation, this.pageViewModel())

        assertThat(document).isEqualTo(Document("../", "Home", "Home", "Home Some info"))
    }

    @Test
    fun `no workspace sections`() {
        val documents = workspaceSections(workspace.documentation, this.pageViewModel())

        assertThat(documents).isEmpty()
    }

    @Test
    fun `indexes non home workspace sections`() {
        workspace.documentation.addSection(createSection("# Home\nSome info"))
        workspace.documentation.addSection(createSection("# Landscape\nMore info"))
        val documents = workspaceSections(workspace.documentation, this.pageViewModel())

        assertThat(documents).containsAtLeast(
            Document(
                "../landscape/",
                "Workspace Documentation",
                "Landscape",
                "Landscape More info"
            )
        )
    }

    @Test
    fun `no workspace decisions`() {
        val documents = workspaceDecisions(workspace.documentation, this.pageViewModel())

        assertThat(documents).isEmpty()
    }

    @Test
    fun `indexes workspace decisions`() {
        workspace.documentation.addDecision(createDecision())
        val documents = workspaceDecisions(workspace.documentation, this.pageViewModel())

        assertThat(documents).containsAtLeast(
            Document(
                "../decisions/1/",
                "Workspace Decision",
                "Decision 1",
                "Decision 1 Decision 1 content"
            )
        )
    }

    @Test
    fun `no software system home or properties`() {
        workspace.model.addSoftwareSystem("Software System 1", "One system to rule them all")
        val document = softwareSystemHome(workspace.model.softwareSystems.single(), this.pageViewModel())

        assertThat(document).isNull()
    }

    @Test
    fun `indexes software system with home section`() {
        workspace.model.addSoftwareSystem("Software System 1", "One system to rule them all").apply {
            documentation.addSection(Section(Format.Markdown, "# Introduction\nSome info"))
        }
        val document = softwareSystemHome(workspace.model.softwareSystems.single(), this.pageViewModel())

        assertThat(document).isEqualTo(
            Document(
                "../software-system-1/",
                "Software System Info",
                "Software System 1 | Introduction",
                "Introduction Some info"
            )
        )
    }

    @Test
    fun `indexes software system with properties`() {
        workspace.model.addSoftwareSystem("Software System 1", "One system to rule them all").apply {
            addProperty("Prop1", "Value 1")
        }
        val document = softwareSystemHome(workspace.model.softwareSystems.single(), this.pageViewModel())

        assertThat(document).isEqualTo(
            Document(
                "../software-system-1/",
                "Software System Info",
                "Software System 1 | Info",
                "Value 1"
            )
        )
    }

    @Test
    fun `indexes software system with home section and properties`() {
        workspace.model.addSoftwareSystem("Software System 1", "One system to rule them all").apply {
            documentation.addSection(Section(Format.Markdown, "# Introduction\nSome info"))
            addProperty("Prop 1", "Value 1")
            addProperty("Prop 2", "Value 2")
        }
        val document = softwareSystemHome(workspace.model.softwareSystems.single(), this.pageViewModel())

        assertThat(document).isEqualTo(
            Document(
                "../software-system-1/",
                "Software System Info",
                "Software System 1 | Introduction",
                "Introduction Some info Value 1 Value 2"
            )
        )
    }

    @Test
    fun `indexes software system context`() {
        workspace.model.addSoftwareSystem("Software System 1", "One system to rule them all")
        val document = softwareSystemContext(workspace.model.softwareSystems.single(), this.pageViewModel())

        assertThat(document).isEqualTo(
            Document(
                "../software-system-1/context/",
                "Context views",
                "Software System 1 | Context views",
                "Software System 1 One system to rule them all"
            )
        )
    }

    @Test
    fun `no software system containers`() {
        workspace.model.addSoftwareSystem("Software System 1", "One system to rule them all")
        val document = softwareSystemContainers(workspace.model.softwareSystems.single(), this.pageViewModel())

        assertThat(document).isNull()
    }

    @Test
    fun `indexes software system containers`() {
        workspace.model.addSoftwareSystem("Software System 1", "One system to rule them all").apply {
            addContainer("Container 1", "a container")
            addContainer("Container 2", "a second container", "tech 1")
        }
        val document = softwareSystemContainers(workspace.model.softwareSystems.single(), this.pageViewModel())

        assertThat(document).isEqualTo(
            Document(
                "../software-system-1/container/",
                "Container views",
                "Software System 1 | Container views",
                "Container 1 a container Container 2 a second container tech 1"
            )
        )
    }

    @Test
    fun `no software system components`() {
        workspace.model.addSoftwareSystem("Software System 1", "One system to rule them all")
        val document = softwareSystemComponents(workspace.model.softwareSystems.single(), this.pageViewModel())

        assertThat(document).isNull()
    }

    @Test
    fun `indexes software system components`() {
        workspace.model.addSoftwareSystem("Software System 1", "One system to rule them all").apply {
            addContainer("Container 1", "a container").apply {
                addComponent("Component 1", "a component")
                addComponent("Component 2", "a second component")
            }
            addContainer("Container 2", "a second container", "tech 1").apply {
                addComponent("Component 3", "a third component", "tech 1")
            }
        }
        val document = softwareSystemComponents(workspace.model.softwareSystems.single(), this.pageViewModel())

        assertThat(document).isEqualTo(
            Document(
                "../software-system-1/component/",
                "Component views",
                "Software System 1 | Component views",
                "Component 1 a component Component 2 a second component Component 3 a third component tech 1"
            )
        )
    }

    @Test
    fun `no software system relationships`() {
        workspace.model.addSoftwareSystem("Software System 1", "One system to rule them all")
        val document = softwareSystemRelationships(workspace.model.softwareSystems.single(), this.pageViewModel())

        assertThat(document).isNull()
    }

    @Test
    fun `indexes software system relationships`() {
        val system1 = workspace.model.addSoftwareSystem("Software System 1", "One system to rule them all")
        val system2 = workspace.model.addSoftwareSystem("Software System 2", "One system to rule the others")
        system1.uses(system2, "reads")
        system2.uses(system1, "writes", "tech 1")
        val document = softwareSystemRelationships(
            workspace.model.softwareSystems.single { it.name == "Software System 1" },
            this.pageViewModel()
        )

        assertThat(document).isEqualTo(
            Document(
                "../software-system-1/dependencies/",
                "Dependencies",
                "Software System 1 | Dependencies",
                "Software System 2 reads Software System 2 writes tech 1"
            )
        )
    }

    @Test
    fun `no software system decisions`() {
        workspace.model.addSoftwareSystem("Software System 1", "One system to rule them all")
        val documents = softwareSystemDecisions(workspace.model.softwareSystems.single(), this.pageViewModel())

        assertThat(documents).isEmpty()
    }

    @Test
    fun `indexes software system decisions`() {
        workspace.model.addSoftwareSystem("Software System 1", "One system to rule them all").apply {
            documentation.addDecision(createDecision("1"))
            documentation.addDecision(createDecision("2"))
        }
        val documents = softwareSystemDecisions(workspace.model.softwareSystems.single(), this.pageViewModel())

        assertThat(documents).containsAtLeast(
            Document(
                "../software-system-1/decisions/1/",
                "Software System Decision",
                "Software System 1 | Decision 1",
                "Decision 1 Decision 1 content"
            ),
            Document(
                "../software-system-1/decisions/2/",
                "Software System Decision",
                "Software System 1 | Decision 2",
                "Decision 2 Decision 2 content"
            )
        )
    }

    @Test
    fun `no software system sections`() {
        workspace.model.addSoftwareSystem("Software System 1", "One system to rule them all")
        val documents = softwareSystemSections(workspace.model.softwareSystems.single(), this.pageViewModel())

        assertThat(documents).isEmpty()
    }

    @Test
    fun `only software system section for software system home`() {
        workspace.model.addSoftwareSystem("Software System 1", "One system to rule them all").apply {
            documentation.addSection(Section(Format.Markdown, "# Introduction\nSome info"))
        }
        val documents = softwareSystemSections(workspace.model.softwareSystems.single(), this.pageViewModel())

        assertThat(documents).isEmpty()
    }

    @Test
    fun `indexes software system sections`() {
        workspace.model.addSoftwareSystem("Software System 1", "One system to rule them all").apply {
            documentation.addSection(Section(Format.Markdown, "# Introduction\nSome info"))
            documentation.addSection(Section(Format.Markdown, "# Usage\nThat's how it works"))
            documentation.addSection(Section(Format.Markdown, "# History\nThat's how we got here"))
        }
        val documents = softwareSystemSections(workspace.model.softwareSystems.single(), this.pageViewModel())

        assertThat(documents).containsAtLeast(
            Document(
                "../software-system-1/sections/usage/",
                "Software System Documentation",
                "Software System 1 | Usage",
                "Usage That's how it works"
            ),
            Document(
                "../software-system-1/sections/history/",
                "Software System Documentation",
                "Software System 1 | History",
                "History That's how we got here"
            )
        )
    }

    @Test
    fun `no container sections`() {
        val softwareSystem = workspace.model.addSoftwareSystem("Software System 1", "One system to rule them all")
        softwareSystem.addContainer("Container 1", "a container")
        val documents = softwareSystemContainerSections(softwareSystem.containers.single(), this.pageViewModel())

        assertThat(documents).isEmpty()
    }

    @Test
    fun `no container decisions`() {
        val softwareSystem = workspace.model.addSoftwareSystem("Software System 1", "One system to rule them all")
        softwareSystem.addContainer("Container 1", "a container")
        val documents = softwareSystemContainerDecisions(softwareSystem.containers.single(), this.pageViewModel())

        assertThat(documents).isEmpty()
    }

    @Test
    fun `indexes container decisions`() {
        val softwareSystem = workspace.model.addSoftwareSystem("Software System 1", "One system to rule them all")
        val container = softwareSystem.addContainer("Container 1", "a container").apply {
            documentation.addDecision(createDecision("1"))
            documentation.addDecision(createDecision("2"))
        }
        val documents = softwareSystemContainerDecisions(container, this.pageViewModel())

        assertThat(documents).containsAtLeast(
            Document(
                "../software-system-1/decisions/container-1/1/",
                "Container Decision",
                "Container 1 | Decision 1",
                "Decision 1 Decision 1 content"
            ),
            Document(
                "../software-system-1/decisions/container-1/2/",
                "Container Decision",
                "Container 1 | Decision 2",
                "Decision 2 Decision 2 content"
            )
        )
    }

    @Test
    fun `indexes container sections`() {
        val softwareSystem = workspace.model.addSoftwareSystem("Software System 1", "One system to rule them all")
        val container = softwareSystem.addContainer("Container 1", "a container").apply {
            documentation.addSection(Section(Format.Markdown, "# Introduction\nSome info"))
            documentation.addSection(Section(Format.Markdown, "# Usage\nThat's how it works"))
            documentation.addSection(Section(Format.Markdown, "# History\nThat's how we got here"))
        }
        val documents = softwareSystemContainerSections(container, this.pageViewModel())

        assertThat(documents).containsAtLeast(
            Document(
                "../software-system-1/sections/container-1/usage/",
                "Container Documentation",
                "Container 1 | Usage",
                "Usage That's how it works"
            ),
            Document(
                "../software-system-1/sections/container-1/history/",
                "Container Documentation",
                "Container 1 | History",
                "History That's how we got here"
            )
        )
    }
}
