package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.structurizr.model.Container
import com.structurizr.model.SoftwareSystem
import kotlin.test.Test

class SoftwareSystemDecisionsPageViewModelTest : ViewModelTest() {
    private val generatorContext = generatorContext()
    private val softwareSystem: SoftwareSystem = generatorContext.workspace.model.addSoftwareSystem("Software system")
    private val container: Container = softwareSystem.addContainer("API Application")

    @Test
    fun `active tab`() {
        val viewModel = SoftwareSystemDecisionsPageViewModel(generatorContext, softwareSystem)

        assertThat(viewModel.tabs.single { it.link.active }.tab)
            .isEqualTo(SoftwareSystemPageViewModel.Tab.DECISIONS)
    }

    @Test
    fun `decisions tabs`() {
        softwareSystem.documentation.addDecision(createDecision("1", "Accepted"))
        container.documentation.addDecision(createDecision("1", "Accepted"))

        val viewModel = SoftwareSystemDecisionsPageViewModel(generatorContext, softwareSystem)

        assertThat(viewModel.decisionTabs)
            .isEqualTo(listOf(
                DecisionTabViewModel(
                    viewModel,
                    "System",
                    "/software-system/decisions"
                ),
                DecisionTabViewModel(
                    viewModel,
                    "API Application",
                    "/software-system/decisions/api-application",
                    Match.CHILD
                )
            ))
    }

    @Test
    fun `decisions tabs without container decisions`() {
        softwareSystem.documentation.addDecision(createDecision("1", "Accepted"))

        val viewModel = SoftwareSystemDecisionsPageViewModel(generatorContext, softwareSystem)

        assertThat(viewModel.decisionTabs)
            .isEqualTo(listOf(
                DecisionTabViewModel(
                    viewModel,
                    "System",
                    "/software-system/decisions"
                )
            ))
    }

    @Test
    fun `decisions tabs without software system decisions`() {
        container.documentation.addDecision(createDecision("1", "Accepted"))

        val viewModel = SoftwareSystemDecisionsPageViewModel(generatorContext, softwareSystem)

        assertThat(viewModel.decisionTabs)
            .isEqualTo(listOf(
                DecisionTabViewModel(
                    viewModel,
                    "API Application",
                    "/software-system/decisions/api-application",
                    Match.CHILD
                )
            ))
    }

    @Test
    fun `decisions table`() {
        softwareSystem.documentation.addDecision(createDecision("1", "Accepted"))

        val viewModel = SoftwareSystemDecisionsPageViewModel(generatorContext, softwareSystem)

        assertThat(viewModel.decisionsTable)
            .isEqualTo(
                viewModel.createDecisionsTableViewModel(softwareSystem.documentation.decisions) {
                    "/software-system/decisions/1"
                }
            )
    }

    @Test
    fun `is visible`() {
        softwareSystem.documentation.addDecision(createDecision("1", "Accepted"))

        val viewModel = SoftwareSystemDecisionsPageViewModel(generatorContext, softwareSystem)

        assertThat(viewModel.visible).isTrue()
        assertThat(viewModel.onlyContainersDecisionsVisible).isFalse()
    }

    @Test
    fun `only container decisions visible`() {
        container.documentation.addDecision(createDecision("1", "Accepted"))

        val viewModel = SoftwareSystemDecisionsPageViewModel(generatorContext, softwareSystem)

        assertThat(viewModel.visible).isTrue()
        assertThat(viewModel.onlyContainersDecisionsVisible).isTrue()
    }

    @Test
    fun `hidden view`() {
        val viewModel = SoftwareSystemDecisionsPageViewModel(generatorContext, softwareSystem)

        assertThat(viewModel.visible).isFalse()
        assertThat(viewModel.onlyContainersDecisionsVisible).isFalse()
    }
}
