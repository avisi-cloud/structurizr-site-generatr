package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.*
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
                "deployment-1",
                "Software system - Deployment - Default",
                "Deployment view 1",
                """<svg viewBox="0 0 800 900"></svg>""",
                800,
                ImageViewModel(viewModel, "/svg/deployment-1.svg"),
                ImageViewModel(viewModel, "/png/deployment-1.png"),
                ImageViewModel(viewModel, "/puml/deployment-1.puml"),
                LegendViewModel(
                    """<svg viewBox="0 0 800 900"></svg>""",
                    800,
                    ImageViewModel(viewModel, "/svg/deployment-1.legend.svg"),
                    ImageViewModel(viewModel, "/png/deployment-1.legend.png"),
                    ImageViewModel(viewModel, "/puml/deployment-1.legend.puml"),
                )
            ),
            DiagramViewModel(
                "deployment-2",
                "Software system - Deployment - Default",
                "Deployment view 2",
                """<svg viewBox="0 0 800 900"></svg>""",
                800,
                ImageViewModel(viewModel, "/svg/deployment-2.svg"),
                ImageViewModel(viewModel, "/png/deployment-2.png"),
                ImageViewModel(viewModel, "/puml/deployment-2.puml"),
                LegendViewModel(
                    """<svg viewBox="0 0 800 900"></svg>""",
                    800,
                    ImageViewModel(viewModel, "/svg/deployment-2.legend.svg"),
                    ImageViewModel(viewModel, "/png/deployment-2.legend.png"),
                    ImageViewModel(viewModel, "/puml/deployment-2.legend.puml"),
                )
            )
        )
    }

    @Test
    fun `hidden view`() {
        val viewModel = SoftwareSystemDeploymentPageViewModel(
            generatorContext,
            generatorContext.workspace.model.addSoftwareSystem("Software system 2")
        )

        assertThat(viewModel.visible).isFalse()
    }

    @Test
    fun `no index`() {
        val viewModel = SoftwareSystemDeploymentPageViewModel(generatorContext, generatorContext.workspace.model
            .addSoftwareSystem("Software system 2").also {
                generatorContext.workspace.views.createDeploymentView(it, "deployment-3", "Deployment view 3")
            })
        assertThat(viewModel.diagramIndex.visible).isFalse()
    }

    @Test
    fun `has index`() {
        val viewModel = SoftwareSystemDeploymentPageViewModel(generatorContext, softwareSystem)
        assertThat(viewModel.diagramIndex.visible).isTrue()
    }

    @Test
    fun `includes star-scoped deployment view when belongsTo in properties`() {
        val viewModel = SoftwareSystemDeploymentPageViewModel(
            generatorContext, generatorContext.workspace.model.addSoftwareSystem("Software system 3").also {
                generatorContext.workspace.views.createDeploymentView("star-scoped", "Star Scoped Deployment View").apply {
                    addProperty("generatr.view.deployment.belongsTo", it.name)
                }
            })

        assertThat(viewModel.visible, "is visible").isTrue()

        assertThat(viewModel.diagrams).exactly(1) { assert ->
            assert.transform { it.key }.isEqualTo("star-scoped")
        }
    }
}
