package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.structurizr.model.Component
import com.structurizr.model.Container
import com.structurizr.model.SoftwareSystem
import kotlin.test.Test

class SoftwareSystemContainerComponentDecisionsPageViewModelTest : ViewModelTest() {
    private val generatorContext = generatorContext()
    private val softwareSystem: SoftwareSystem = generatorContext.workspace.model.addSoftwareSystem("Software system")
    private val container: Container = softwareSystem.addContainer("API Application")
    private val component: Component = container.addComponent("Email Component")

    @Test
    fun `active tab`() {
        val viewModel = SoftwareSystemContainerComponentDecisionsPageViewModel(generatorContext, component)

        assertThat(viewModel.tabs.single { it.link.active }.tab)
            .isEqualTo(SoftwareSystemPageViewModel.Tab.DECISIONS)
    }

    @Test
    fun `decisions tabs`() {
        softwareSystem.documentation.addDecision(createDecision("1", "Accepted"))

        container.documentation.addDecision(createDecision("1", "Accepted"))

        val viewModel = SoftwareSystemContainerComponentDecisionsPageViewModel(generatorContext, component)

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

        val viewModel = SoftwareSystemContainerComponentDecisionsPageViewModel(generatorContext, component)

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

        val viewModel = SoftwareSystemContainerComponentDecisionsPageViewModel(generatorContext, component)

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
    fun `component decisions tabs`() {
        container.documentation.addDecision(createDecision("1", "Accepted"))
        createComponentDecisions()

        val viewModel = SoftwareSystemContainerComponentDecisionsPageViewModel(generatorContext, component)

        assertThat(viewModel.componentDecisionsTabs)
            .isEqualTo(listOf(
                DecisionTabViewModel(
                    viewModel,
                    "Container",
                    "/software-system/decisions/api-application"
                ),
                DecisionTabViewModel(
                    viewModel,
                    "Email Component",
                    "/software-system/decisions/api-application/email-component"
                )
            ))
    }

    @Test
    fun `component decisions tabs without container decisions`() {
        createComponentDecisions()

        val viewModel = SoftwareSystemContainerComponentDecisionsPageViewModel(generatorContext, component)

        assertThat(viewModel.componentDecisionsTabs)
            .isEqualTo(listOf(
                DecisionTabViewModel(
                    viewModel,
                    "Email Component",
                    "/software-system/decisions/api-application/email-component"
                )
            ))
    }

    @Test
    fun `component decisions tabs without component decisions`() {
        container.documentation.addDecision(createDecision("1", "Accepted"))

        val viewModel = SoftwareSystemContainerComponentDecisionsPageViewModel(generatorContext, component)

        assertThat(viewModel.componentDecisionsTabs)
            .isEqualTo(listOf(
                DecisionTabViewModel(
                    viewModel,
                    "Container",
                    "/software-system/decisions/api-application"
                )
            ))
    }

    @Test
    fun `decisions table`() {
        createComponentDecisions()

        val viewModel = SoftwareSystemContainerComponentDecisionsPageViewModel(generatorContext, component)

        var counter = 1

        assertThat(viewModel.decisionsTable)
            .isEqualTo(
                viewModel.createDecisionsTableViewModel(component.documentation.decisions) {
                    "/software-system/decisions/api-application/email-component/${counter++}"
                }
            )
    }

    @Test
    fun `is visible`() {
        createComponentDecisions()

        val viewModel = SoftwareSystemContainerComponentDecisionsPageViewModel(generatorContext, component)

        assertThat(viewModel.visible).isTrue()
    }

    @Test
    fun `hidden view`() {
        val viewModel = SoftwareSystemContainerComponentDecisionsPageViewModel(generatorContext, component)

        assertThat(viewModel.visible).isFalse()
    }

    private fun createComponentDecisions() {
        component.documentation.addDecision(createDecision(
            "1",
            "Accepted"
        ))
        component.documentation.addDecision(createDecision(
            "2",
            "Superseded by [3. Another Realisation of Feature 1](0003-another-realisation-of-feature-1.md)"
        ))
        component.documentation.addDecision(createDecision(
            "3",
            "Accepted\n\nSupersedes [2. Implement Feature 1](0002-implement-feature-1.md)"
        ))
    }
}
