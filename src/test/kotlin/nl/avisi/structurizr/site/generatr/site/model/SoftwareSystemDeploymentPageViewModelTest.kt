package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEqualTo
import com.structurizr.model.SoftwareSystem
import kotlin.test.Test

class SoftwareSystemDeploymentPageViewModelTest : ViewModelTest() {
    private val generatorContext = generatorContext()
    private val softwareSystem: SoftwareSystem = generatorContext.workspace.model
        .addSoftwareSystem("Software system").also {
            generatorContext.workspace.views.createDeploymentView(it, "deployment-1", "Deployment view 1")
            generatorContext.workspace.views.createDeploymentView(it, "deployment-2", "Deployment view 2")
        }

    @Test
    fun `active tab`() {
        val viewModel = SoftwareSystemDeploymentPageViewModel(generatorContext, softwareSystem)

        assertThat(viewModel.tabs.single { it.link.active }.tab)
            .isEqualTo(SoftwareSystemPageViewModel.Tab.DEPLOYMENT)
    }

    @Test
    fun `diagrams sorted by key`() {
        val viewModel = SoftwareSystemDeploymentPageViewModel(generatorContext, softwareSystem)

        assertThat(viewModel.diagrams).containsExactly(
            DiagramViewModel(
                "Software system - Deployment - Default",
                ImageViewModel(viewModel, "/svg/deployment-1.svg"),
                ImageViewModel(viewModel, "/png/deployment-1.png"),
                ImageViewModel(viewModel, "/puml/deployment-1.puml")
            ),
            DiagramViewModel(
                "Software system - Deployment - Default",
                ImageViewModel(viewModel, "/svg/deployment-2.svg"),
                ImageViewModel(viewModel, "/png/deployment-2.png"),
                ImageViewModel(viewModel, "/puml/deployment-2.puml")
            )
        )
    }
}
