package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.*
import nl.avisi.structurizr.site.generatr.site.model.SoftwareSystemPageViewModel.Tab
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import kotlin.test.Test

class SoftwareSystemPageViewModelTest : ViewModelTest() {
    @TestFactory
    fun url() = listOf(
        Tab.HOME to "/software-system-with-1-name",
        Tab.SYSTEM_CONTEXT to "/software-system-with-1-name/context",
        Tab.CONTAINER to "/software-system-with-1-name/container",
        Tab.COMPONENT to "/software-system-with-1-name/component",
        Tab.DEPLOYMENT to "/software-system-with-1-name/deployment",
        Tab.DEPENDENCIES to "/software-system-with-1-name/dependencies",
        Tab.DECISIONS to "/software-system-with-1-name/decisions",
    ).map { (tab, expectedUrl) ->
        DynamicTest.dynamicTest("url - $tab") {
            val generatorContext = generatorContext()
            val softwareSystem = generatorContext.workspace.model.addSoftwareSystem("Software system With 1 name")

            assertThat(SoftwareSystemPageViewModel.url(softwareSystem, tab))
                .isEqualTo(expectedUrl)
            assertThat(SoftwareSystemPageViewModel(generatorContext, softwareSystem, tab).url)
                .isEqualTo(SoftwareSystemPageViewModel.url(softwareSystem, tab))
        }
    }

    @TestFactory
    fun subtitle() = Tab.entries.map { tab ->
        DynamicTest.dynamicTest("subtitle - $tab") {
            val generatorContext = generatorContext()
            val softwareSystem = generatorContext.workspace.model.addSoftwareSystem("Some software system")

            assertThat(SoftwareSystemPageViewModel(generatorContext, softwareSystem, tab).pageSubTitle)
                .isEqualTo("Some software system")
        }
    }

    @Test
    fun `software system description`() {
        val generatorContext = generatorContext()
        val softwareSystem = generatorContext.workspace.model.addSoftwareSystem("System", "Description")
        val viewModel = SoftwareSystemPageViewModel(generatorContext, softwareSystem, Tab.HOME)

        assertThat(viewModel.description).isEqualTo("Description")
    }

    @Test
    fun tabs() {
        val generatorContext = generatorContext()
        val softwareSystem = generatorContext.workspace.model.addSoftwareSystem("Some software system")
        val viewModel = SoftwareSystemPageViewModel(generatorContext, softwareSystem, Tab.HOME)

        assertThat(viewModel.tabs.map { it.tab })
            .containsExactly(
                Tab.HOME,
                Tab.SYSTEM_CONTEXT,
                Tab.CONTAINER,
                Tab.COMPONENT,
                Tab.CODE,
                Tab.DYNAMIC,
                Tab.DEPLOYMENT,
                Tab.DEPENDENCIES,
                Tab.DECISIONS,
                Tab.SECTIONS
            )
        assertThat(viewModel.tabs.map { it.link.title })
            .containsExactly(
                "Info",
                "Context views",
                "Container views",
                "Component views",
                "Code views",
                "Dynamic views",
                "Deployment views",
                "Dependencies",
                "Decisions",
                "Documentation"
            )
    }

    @TestFactory
    fun `active tab`() = Tab.entries
        .filter { it != Tab.COMPONENT } // Component link is dynamic
        .map { tab ->
            DynamicTest.dynamicTest("active tab - $tab") {
                val generatorContext = generatorContext()
                val softwareSystem = generatorContext.workspace.model.addSoftwareSystem("Some software system")
                val viewModel = SoftwareSystemPageViewModel(generatorContext, softwareSystem, tab)

                assertThat(
                    viewModel.tabs
                        .single { it.link.active }
                        .tab
                ).isEqualTo(tab)
            }
        }

    @Test
    fun `home tab is visible`() {
        val generatorContext = generatorContext()
        val softwareSystem = generatorContext.workspace.model.addSoftwareSystem("Some software system")
        val viewModel = SoftwareSystemPageViewModel(generatorContext, softwareSystem, Tab.HOME)

        assertThat(getTab(viewModel, Tab.HOME).visible).isTrue()
    }

    @Test
    fun `dependencies tab is visible`() {
        val generatorContext = generatorContext()
        val softwareSystem = generatorContext.workspace.model.addSoftwareSystem("Some software system")
        val viewModel = SoftwareSystemPageViewModel(generatorContext, softwareSystem, Tab.HOME)

        assertThat(getTab(viewModel, Tab.DEPENDENCIES).visible).isTrue()
    }

    @Test
    fun `context views tab only visible when context diagrams available`() {
        val generatorContext = generatorContext()
        val softwareSystem = generatorContext.workspace.model.addSoftwareSystem("Some software system")
        val viewModel = SoftwareSystemPageViewModel(generatorContext, softwareSystem, Tab.HOME)

        assertThat(getTab(viewModel, Tab.SYSTEM_CONTEXT).visible).isFalse()
        generatorContext.workspace.views.createSystemContextView(softwareSystem, "context", "description")
        assertThat(getTab(viewModel, Tab.SYSTEM_CONTEXT).visible).isTrue()
    }

    @Test
    fun `container views tab only visible when container diagrams available`() {
        val generatorContext = generatorContext()
        val softwareSystem = generatorContext.workspace.model.addSoftwareSystem("Some software system")
        val viewModel = SoftwareSystemPageViewModel(generatorContext, softwareSystem, Tab.HOME)

        assertThat(getTab(viewModel, Tab.CONTAINER).visible).isFalse()
        generatorContext.workspace.views.createContainerView(softwareSystem, "container", "description")
        assertThat(getTab(viewModel, Tab.CONTAINER).visible).isTrue()
    }

    @Test
    fun `component views tab only visible when component diagrams available`() {
        val generatorContext = generatorContext()
        val softwareSystem = generatorContext.workspace.model.addSoftwareSystem("Some software system")
        val container = softwareSystem.addContainer("Backend")
        val viewModel = SoftwareSystemPageViewModel(generatorContext, softwareSystem, Tab.HOME)

        assertThat(getTab(viewModel, Tab.COMPONENT).visible).isFalse()
        generatorContext.workspace.views.createComponentView(container, "component", "description")
        assertThat(getTab(viewModel, Tab.COMPONENT).visible).isTrue()
    }

    @Test
    fun `dynamic views tab only visible when dynamic diagrams available`() {
        val generatorContext = generatorContext()
        val softwareSystem = generatorContext.workspace.model.addSoftwareSystem("Some software system")
        val container = softwareSystem.addContainer("Backend")
        val viewModel = SoftwareSystemPageViewModel(generatorContext, softwareSystem, Tab.HOME)

        assertThat(getTab(viewModel, Tab.DYNAMIC).visible).isFalse()
        generatorContext.workspace.views.createDynamicView(container, "component", "description")
        assertThat(getTab(viewModel, Tab.DYNAMIC).visible).isTrue()
    }

    @Test
    fun `deployment views tab only visible when deployment diagrams available`() {
        val generatorContext = generatorContext()
        val softwareSystem = generatorContext.workspace.model.addSoftwareSystem("Some software system")
        val viewModel = SoftwareSystemPageViewModel(generatorContext, softwareSystem, Tab.HOME)

        assertThat(getTab(viewModel, Tab.DEPLOYMENT).visible).isFalse()
        generatorContext.workspace.views.createDeploymentView(softwareSystem, "deployment", "description")
        assertThat(getTab(viewModel, Tab.DEPLOYMENT).visible).isTrue()
    }

    @Test
    fun `decisions views tab visible when software system decisions available`() {
        val generatorContext = generatorContext()
        val softwareSystem = generatorContext.workspace.model.addSoftwareSystem("Some software system")
        val viewModel = SoftwareSystemPageViewModel(generatorContext, softwareSystem, Tab.HOME)

        assertThat(getTab(viewModel, Tab.DECISIONS).visible).isFalse()
        softwareSystem.documentation.addDecision(createDecision("1", "Proposed"))
        assertThat(getTab(viewModel, Tab.DECISIONS).visible).isTrue()
    }

    @Test
    fun `decisions views tab visible when container decisions available in software system`() {
        val generatorContext = generatorContext()
        val softwareSystem = generatorContext.workspace.model.addSoftwareSystem("Some software system")
        val viewModel = SoftwareSystemPageViewModel(generatorContext, softwareSystem, Tab.HOME)

        assertThat(getTab(viewModel, Tab.DECISIONS).visible).isFalse()
        softwareSystem.addContainer("Some Container").documentation.addDecision(createDecision("2", "Proposed"))
        assertThat(getTab(viewModel, Tab.DECISIONS).visible).isTrue()
    }

    @Test
    fun `decisions views tab visible when container & software system decisions are available`() {
        val generatorContext = generatorContext()
        val softwareSystem = generatorContext.workspace.model.addSoftwareSystem("Some software system")
        val viewModel = SoftwareSystemPageViewModel(generatorContext, softwareSystem, Tab.HOME)

        assertThat(getTab(viewModel, Tab.DECISIONS).visible).isFalse()
        softwareSystem.addContainer("Some Container").documentation.addDecision(createDecision("2", "Proposed"))
        softwareSystem.documentation.addDecision(createDecision("1", "Proposed"))
        assertThat(getTab(viewModel, Tab.DECISIONS).visible).isTrue()
    }

    @Test
    fun `sections views tab only visible when two or more sections available`() {
        val generatorContext = generatorContext()
        val softwareSystem = generatorContext.workspace.model.addSoftwareSystem("Some software system")
        val viewModel = SoftwareSystemPageViewModel(generatorContext, softwareSystem, Tab.HOME)

        assertThat(getTab(viewModel, Tab.SECTIONS).visible).isFalse()
        softwareSystem.documentation.addSection(createSection("# Section 0000"))
        assertThat(getTab(viewModel, Tab.SECTIONS).visible).isFalse()
        softwareSystem.documentation.addSection(createSection("# Section 0001"))
        assertThat(getTab(viewModel, Tab.SECTIONS).visible).isTrue()
    }

    @Test
    fun `sections views tab visible when container sections available in software system`() {
        val generatorContext = generatorContext()
        val softwareSystem = generatorContext.workspace.model.addSoftwareSystem("Some software system")
        val viewModel = SoftwareSystemPageViewModel(generatorContext, softwareSystem, Tab.HOME)

        assertThat(getTab(viewModel, Tab.SECTIONS).visible).isFalse()
        softwareSystem.addContainer("Some Container").documentation.addSection(createSection())
        assertThat(getTab(viewModel, Tab.SECTIONS).visible).isTrue()
    }

    @Test
    fun `sections views tab visible when container & software system sections are available`() {
        val generatorContext = generatorContext()
        val softwareSystem = generatorContext.workspace.model.addSoftwareSystem("Some software system")
        val viewModel = SoftwareSystemPageViewModel(generatorContext, softwareSystem, Tab.HOME)

        assertThat(getTab(viewModel, Tab.SECTIONS).visible).isFalse()
        softwareSystem.addContainer("Some Container").documentation.addSection(createSection())
        repeat(2) { // the first section is shown in the software system home
            softwareSystem.documentation.addSection(createSection())
        }
        assertThat(getTab(viewModel, Tab.SECTIONS).visible).isTrue()
    }

    private fun getTab(viewModel: SoftwareSystemPageViewModel, tab: Tab) =
        viewModel.tabs.single { it.tab == tab }
}
