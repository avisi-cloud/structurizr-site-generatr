package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.structurizr.model.Container
import com.structurizr.model.SoftwareSystem
import nl.avisi.structurizr.site.generatr.normalize
import org.junit.jupiter.api.Test

class SoftwareSystemContainerComponentCodePageViewModelTest : ViewModelTest() {
    private val generatorContext = generatorContext()
    private val softwareSystem: SoftwareSystem = generatorContext.workspace.model
        .addSoftwareSystem("Software system").also {
            val backend = it.addContainer("Backend")
            val frontend = it.addContainer("Frontend")
            generatorContext.workspace.views.createComponentView(backend, "component-1-backend", "Component view 1 - Backend")
            generatorContext.workspace.views.createComponentView(backend, "component-2-backend", "Component view 2 - Backend")

            generatorContext.workspace.views.createComponentView(frontend, "component-1-frontend", "Component view 1 - Frontend")
            generatorContext.workspace.views.createComponentView(frontend, "component-2-frontend", "Component view 2 - Frontend")
        }
    private val backendContainer: Container = softwareSystem.containers.elementAt(0)
    private val frontendContainer: Container = softwareSystem.containers.elementAt(1)
    private val backendComponent = backendContainer.addComponent("Backend Component")
    private val backendComponent2 = backendContainer.addComponent("Java Application")
    private val frontendComponent = frontendContainer.addComponent("Frontend Component")
    private val backendImageView = createImageView(generatorContext.workspace, backendComponent)
    private val backendImageView2 = createImageView(generatorContext.workspace, backendComponent2)
    private val frontendImageView = createImageView(generatorContext.workspace, frontendComponent)

    @Test
    fun `active tab`() {
        val viewModel = SoftwareSystemContainerComponentCodePageViewModel(generatorContext, backendContainer, backendComponent)

        assertThat(viewModel.tabs.single { it.link.active }.tab)
            .isEqualTo(SoftwareSystemPageViewModel.Tab.CODE)
    }

    @Test
    fun `container tabs`() {
        val viewModel = SoftwareSystemContainerComponentCodePageViewModel(generatorContext, backendContainer, backendComponent)
        val containerTabList = listOf(
            ContainerTabViewModel(viewModel, "Backend", "/software-system/code/backend/backend-component", Match.SIBLING),
            ContainerTabViewModel(viewModel, "Frontend", "/software-system/code/frontend/frontend-component", Match.SIBLING)
        )
        assertThat(viewModel.containerTabs.elementAtOrNull(0)).isEqualTo(containerTabList.elementAt(0))
        assertThat(viewModel.containerTabs.elementAtOrNull(1)).isEqualTo(containerTabList.elementAt(1))
    }

    @Test
    fun `component tabs`() {
        val viewModel = SoftwareSystemContainerComponentCodePageViewModel(generatorContext, backendContainer, backendComponent)
        val componentTabList = listOf(
            ComponentTabViewModel(viewModel, "Backend Component", "/software-system/code/backend/backend-component"),
            ComponentTabViewModel(viewModel, "Java Application", "/software-system/code/backend/java-application")
        )
        assertThat(viewModel.componentTabs.elementAtOrNull(0)).isEqualTo(componentTabList.elementAt(0))
        assertThat(viewModel.componentTabs.elementAtOrNull(1)).isEqualTo(componentTabList.elementAt(1))
    }

    @Test
    fun url() {
        var viewModel = SoftwareSystemContainerComponentCodePageViewModel(generatorContext, backendContainer, backendComponent)
        assertThat(SoftwareSystemContainerComponentCodePageViewModel.url(backendContainer, backendComponent))
            .isEqualTo("/${softwareSystem.name.normalize()}/code/${backendContainer.name.normalize()}/${backendComponent.name.normalize()}")
        assertThat(viewModel.url)
            .isEqualTo(SoftwareSystemContainerComponentCodePageViewModel.url(backendContainer, backendComponent))

        viewModel = SoftwareSystemContainerComponentCodePageViewModel(generatorContext, frontendContainer, frontendComponent)
        assertThat(SoftwareSystemContainerComponentCodePageViewModel.url(frontendContainer, frontendComponent))
            .isEqualTo("/${softwareSystem.name.normalize()}/code/${frontendContainer.name.normalize()}/${frontendComponent.name.normalize()}")
        assertThat(viewModel.url)
            .isEqualTo(SoftwareSystemContainerComponentCodePageViewModel.url(frontendContainer, frontendComponent))
    }

    @Test
    fun `has image`() {
        var viewModel = SoftwareSystemContainerComponentCodePageViewModel(generatorContext, backendContainer, backendComponent)

        assertThat(viewModel.visible).isTrue()
        assertThat(viewModel.images).hasSize(1)
        assertThat(viewModel.images.single().imageView).isEqualTo(backendImageView)

        viewModel = SoftwareSystemContainerComponentCodePageViewModel(generatorContext, backendContainer, backendComponent2)

        assertThat(viewModel.visible).isTrue()
        assertThat(viewModel.images).hasSize(1)
        assertThat(viewModel.images.single().imageView).isEqualTo(backendImageView2)

        viewModel = SoftwareSystemContainerComponentCodePageViewModel(generatorContext, frontendContainer, frontendComponent)

        assertThat(viewModel.visible).isTrue()
        assertThat(viewModel.images).hasSize(1)
        assertThat(viewModel.images.single().imageView).isEqualTo(frontendImageView)
    }

    @Test
    fun `hidden view`() {
        val viewModel = SoftwareSystemContainerComponentCodePageViewModel(
            generatorContext,
            softwareSystem.addContainer("Container"),
            backendContainer.addComponent("Component")
        )

        assertThat(viewModel.visible).isFalse()
    }

    @Test
    fun `show list is disabled`() {
        val viewModel = SoftwareSystemContainerComponentCodePageViewModel(generatorContext, backendContainer, backendComponent)
        assertThat(viewModel.diagramIndex.showList).isFalse()


    }
}
