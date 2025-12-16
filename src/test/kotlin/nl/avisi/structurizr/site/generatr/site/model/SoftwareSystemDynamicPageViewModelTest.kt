package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.structurizr.model.SoftwareSystem
import kotlin.test.Test

class SoftwareSystemDynamicPageViewModelTest : ViewModelTest() {
    private val generatorContext = generatorContext()
    private val softwareSystem: SoftwareSystem = generatorContext.workspace.model
        .addSoftwareSystem("Software system").also {
            val backend = it.addContainer("Backend")
            val frontend = it.addContainer("Frontend")
            generatorContext.workspace.views.createDynamicView(backend, "backend-dynamic", "Dynamic view 1")
            generatorContext.workspace.views.createDynamicView(frontend, "frontend-dynamic", "Dynamic view 2")
        }

    @Test
    fun `active tab`() {
        val viewModel = SoftwareSystemDynamicPageViewModel(generatorContext, softwareSystem)

        assertThat(viewModel.tabs.single { it.link.active }.tab)
            .isEqualTo(SoftwareSystemPageViewModel.Tab.DYNAMIC)
    }

    @Test
    fun `diagrams sorted by key`() {
        val viewModel = SoftwareSystemDynamicPageViewModel(generatorContext, softwareSystem)

        assertThat(viewModel.diagrams).containsExactly(
            DiagramViewModel(
                "backend-dynamic",
                "Backend - Dynamic",
                "Dynamic view 1",
                """<svg viewBox="0 0 800 900"></svg>""",
                800,
                ImageViewModel(viewModel, "/svg/backend-dynamic.svg"),
                ImageViewModel(viewModel, "/png/backend-dynamic.png"),
                ImageViewModel(viewModel, "/puml/backend-dynamic.puml"),
                LegendViewModel(
                    """<svg viewBox="0 0 800 900"></svg>""",
                    800,
                    ImageViewModel(viewModel, "/svg/backend-dynamic.legend.svg"),
                    ImageViewModel(viewModel, "/png/backend-dynamic.legend.png"),
                    ImageViewModel(viewModel, "/puml/backend-dynamic.legend.puml"),
                )
            ),
            DiagramViewModel(
                "frontend-dynamic",
                "Frontend - Dynamic",
                "Dynamic view 2",
                """<svg viewBox="0 0 800 900"></svg>""",
                800,
                ImageViewModel(viewModel, "/svg/frontend-dynamic.svg"),
                ImageViewModel(viewModel, "/png/frontend-dynamic.png"),
                ImageViewModel(viewModel, "/puml/frontend-dynamic.puml"),
                LegendViewModel(
                    """<svg viewBox="0 0 800 900"></svg>""",
                    800,
                    ImageViewModel(viewModel, "/svg/frontend-dynamic.legend.svg"),
                    ImageViewModel(viewModel, "/png/frontend-dynamic.legend.png"),
                    ImageViewModel(viewModel, "/puml/frontend-dynamic.legend.puml"),
                )
            )
        )
    }

    @Test
    fun `hidden view`() {
        val viewModel = SoftwareSystemDynamicPageViewModel(
            generatorContext,
            generatorContext.workspace.model.addSoftwareSystem("Software system 2")
        )

        assertThat(viewModel.visible).isFalse()
    }

    @Test
    fun `no index`() {
        val viewModel = SoftwareSystemDynamicPageViewModel(generatorContext, generatorContext.workspace.model
            .addSoftwareSystem("Software system 2").also {
                val backend = it.addContainer("Api")
                generatorContext.workspace.views.createDynamicView(backend, "api-dynamic", "Dynamic view 3")
            })
        assertThat(viewModel.diagramIndex.visible).isFalse()
    }

    @Test
    fun `has index`() {
        val viewModel = SoftwareSystemDynamicPageViewModel(generatorContext, softwareSystem)
        assertThat(viewModel.diagramIndex.visible).isTrue()
    }
}
