package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.*
import com.structurizr.model.Container
import com.structurizr.model.SoftwareSystem
import nl.avisi.structurizr.site.generatr.normalize
import kotlin.test.Test
import kotlin.test.assertTrue

class SoftwareSystemContainerComponentsPageViewModelTest : ViewModelTest() {
    private val generatorContext = generatorContext()
    private val softwareSystem: SoftwareSystem = generatorContext.workspace.model
        .addSoftwareSystem("Software system").also {
            val backend = it.addContainer("Backend")
            val frontend = it.addContainer("Frontend")
            it.addContainer("Api").also { c ->
                c.addProperty("test", "value")
            }
            generatorContext.workspace.views.createComponentView(backend, "component-1-backend", "Component view 1 - Backend")
            generatorContext.workspace.views.createComponentView(backend, "component-2-backend", "Component view 2 - Backend")

            generatorContext.workspace.views.createComponentView(frontend, "component-1-frontend", "Component view 1 - Frontend")
            generatorContext.workspace.views.createComponentView(frontend, "component-2-frontend", "Component view 2 - Frontend")
        }

    private val backendContainer: Container = softwareSystem.containers.elementAt(0)
    private val frontendContainer: Container = softwareSystem.containers.elementAt(1)
    private val apiContainer: Container = softwareSystem.containers.elementAt(2)
    private val backendImageView = createImageView(generatorContext.workspace, backendContainer)
    private val frontendImageView = createImageView(generatorContext.workspace, frontendContainer)

    @Test
    fun `active tab`() {
        val viewModel = SoftwareSystemContainerComponentsPageViewModel(generatorContext, backendContainer)
        assertThat(viewModel.tabs.single { it.link.active }.tab)
            .isEqualTo(SoftwareSystemPageViewModel.Tab.COMPONENT)
    }

    @Test
    fun `container tabs`() {
        val viewModel = SoftwareSystemContainerComponentsPageViewModel(generatorContext, backendContainer)
        val componentTabList = listOf(
            ContainerTabViewModel(viewModel, "Api", "/software-system/component/api"),
            ContainerTabViewModel(viewModel, "Backend", "/software-system/component/backend"),
            ContainerTabViewModel(viewModel, "Frontend", "/software-system/component/frontend"))
        assertThat(viewModel.containerTabs.elementAtOrNull(0)).isEqualTo(componentTabList.elementAt(0))
        assertThat(viewModel.containerTabs.elementAtOrNull(1)).isEqualTo(componentTabList.elementAt(1))
        assertThat(viewModel.containerTabs.elementAtOrNull(2)).isEqualTo(componentTabList.elementAt(2))
    }

    @Test
    fun url() {
        var viewModel = SoftwareSystemContainerComponentsPageViewModel(generatorContext, backendContainer)
        assertThat(SoftwareSystemContainerComponentsPageViewModel.url(backendContainer))
            .isEqualTo("/${softwareSystem.name.normalize()}/component/${backendContainer.name.normalize()}")
        assertThat(viewModel.url)
            .isEqualTo(SoftwareSystemContainerComponentsPageViewModel.url(backendContainer))

        viewModel = SoftwareSystemContainerComponentsPageViewModel(generatorContext, frontendContainer)
        assertThat(SoftwareSystemContainerComponentsPageViewModel.url(frontendContainer))
            .isEqualTo("/${softwareSystem.name.normalize()}/component/${frontendContainer.name.normalize()}")
        assertThat(viewModel.url)
            .isEqualTo(SoftwareSystemContainerComponentsPageViewModel.url(frontendContainer))
    }

    @Test
    fun `diagrams correctly mapped to containers`() {
        var viewModel = SoftwareSystemContainerComponentsPageViewModel(generatorContext, backendContainer)
        assertThat(viewModel.visible).isTrue()
        assertThat(viewModel.diagrams).containsExactly(
            DiagramViewModel(
                "component-1-backend",
                "Software system - Backend - Components",
                "Component view 1 - Backend",
                """<svg viewBox="0 0 800 900"></svg>""",
                800,
                ImageViewModel(viewModel, "/svg/component-1-backend.svg"),
                ImageViewModel(viewModel, "/png/component-1-backend.png"),
                ImageViewModel(viewModel, "/puml/component-1-backend.puml"),
                LegendViewModel(
                    """<svg viewBox="0 0 800 900"></svg>""",
                    800,
                    ImageViewModel(viewModel, "/svg/component-1-backend.legend.svg"),
                    ImageViewModel(viewModel, "/png/component-1-backend.legend.png"),
                    ImageViewModel(viewModel, "/puml/component-1-backend.legend.puml"),
                )
            ),
            DiagramViewModel(
                "component-2-backend",
                "Software system - Backend - Components",
                "Component view 2 - Backend",
                """<svg viewBox="0 0 800 900"></svg>""",
                800,
                ImageViewModel(viewModel, "/svg/component-2-backend.svg"),
                ImageViewModel(viewModel, "/png/component-2-backend.png"),
                ImageViewModel(viewModel, "/puml/component-2-backend.puml"),
                LegendViewModel(
                    """<svg viewBox="0 0 800 900"></svg>""",
                    800,
                    ImageViewModel(viewModel, "/svg/component-2-backend.legend.svg"),
                    ImageViewModel(viewModel, "/png/component-2-backend.legend.png"),
                    ImageViewModel(viewModel, "/puml/component-2-backend.legend.puml"),
                )
            )
        )

        viewModel = SoftwareSystemContainerComponentsPageViewModel(generatorContext, frontendContainer)
        assertThat(viewModel.visible).isTrue()
        assertThat(viewModel.diagrams).containsExactly(
            DiagramViewModel(
                "component-1-frontend",
                "Software system - Frontend - Components",
                "Component view 1 - Frontend",
                """<svg viewBox="0 0 800 900"></svg>""",
                800,
                ImageViewModel(viewModel, "/svg/component-1-frontend.svg"),
                ImageViewModel(viewModel, "/png/component-1-frontend.png"),
                ImageViewModel(viewModel, "/puml/component-1-frontend.puml"),
                LegendViewModel(
                    """<svg viewBox="0 0 800 900"></svg>""",
                    800,
                    ImageViewModel(viewModel, "/svg/component-1-frontend.legend.svg"),
                    ImageViewModel(viewModel, "/png/component-1-frontend.legend.png"),
                    ImageViewModel(viewModel, "/puml/component-1-frontend.legend.puml"),
                )
            ),
            DiagramViewModel(
                "component-2-frontend",
                "Software system - Frontend - Components",
                "Component view 2 - Frontend",
                """<svg viewBox="0 0 800 900"></svg>""",
                800,
                ImageViewModel(viewModel, "/svg/component-2-frontend.svg"),
                ImageViewModel(viewModel, "/png/component-2-frontend.png"),
                ImageViewModel(viewModel, "/puml/component-2-frontend.puml"),
                LegendViewModel(
                    """<svg viewBox="0 0 800 900"></svg>""",
                    800,
                    ImageViewModel(viewModel, "/svg/component-2-frontend.legend.svg"),
                    ImageViewModel(viewModel, "/png/component-2-frontend.legend.png"),
                    ImageViewModel(viewModel, "/puml/component-2-frontend.legend.puml"),
                )
            )
        )
    }

    @Test
    fun `has image`() {
        var viewModel = SoftwareSystemContainerComponentsPageViewModel(generatorContext, backendContainer)
        assertThat(viewModel.visible).isTrue()
        assertThat(viewModel.images).hasSize(1)
        assertThat(viewModel.images.single().imageView).isEqualTo(backendImageView)

        viewModel = SoftwareSystemContainerComponentsPageViewModel(generatorContext, frontendContainer)
        assertThat(viewModel.visible).isTrue()
        assertThat(viewModel.images).hasSize(1)
        assertThat(viewModel.images.single().imageView).isEqualTo(frontendImageView)
    }

    @Test
    fun `has only properties`() {
        val viewModel = SoftwareSystemContainerComponentsPageViewModel(generatorContext, apiContainer)
        assertTrue(viewModel.visible)
        assertThat(viewModel.propertiesTable.bodyRows).isNotEmpty()
        assertThat(viewModel.images).isEmpty()
        assertThat(viewModel.diagrams).isEmpty()
    }

    @Test
    fun `hidden view`() {
        val viewModel = SoftwareSystemContainerComponentsPageViewModel(generatorContext, softwareSystem.addContainer("Container"))
        assertThat(viewModel.visible).isFalse()
    }

    @Test
    fun `no index`() {
        val viewModel = SoftwareSystemContainerComponentsPageViewModel(generatorContext, apiContainer)
        assertThat(viewModel.diagramIndex.visible).isFalse()
    }

    @Test
    fun `has index`() {
        val viewModel = SoftwareSystemContainerComponentsPageViewModel(generatorContext, backendContainer)
        assertThat(viewModel.diagramIndex.visible).isTrue()
    }
}
