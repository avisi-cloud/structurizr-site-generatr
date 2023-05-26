package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test

class DecisionTabViewModelTest : ViewModelTest() {

    @Test
    fun `no decision tabs`() {
        val context = generatorContext()
        val softwareSystem = context.workspace.model.addSoftwareSystem("Software system")

        val softwareSystemPageViewModel = SoftwareSystemPageViewModel(context, softwareSystem, SoftwareSystemPageViewModel.Tab.DECISIONS)
        val decisionTabViewModel = softwareSystemPageViewModel.createDecisionsTabViewModel(softwareSystem, SoftwareSystemPageViewModel.Tab.DECISIONS)

        assertThat(decisionTabViewModel.size).isEqualTo(0)
    }

    @Test
    fun `software system decision tab available`() {
        val context = generatorContext()
        val softwareSystem = context.workspace.model.addSoftwareSystem("Software system")
        softwareSystem.documentation.addDecision(createDecision("1", "Accepted"))

        val softwareSystemPageViewModel = SoftwareSystemPageViewModel(context, softwareSystem, SoftwareSystemPageViewModel.Tab.DECISIONS)
        val decisionTabViewModel = softwareSystemPageViewModel.createDecisionsTabViewModel(softwareSystem, SoftwareSystemPageViewModel.Tab.DECISIONS)

        assertThat(decisionTabViewModel.size).isEqualTo(1)
        assertThat(decisionTabViewModel.first().title).isEqualTo("System")
    }

    @Test
    fun `container decision tab available `() {
        val context = generatorContext()
        val softwareSystem = context.workspace.model.addSoftwareSystem("Software system")
        softwareSystem.addContainer("Some Container").documentation.addDecision(createDecision("2", "Proposed"))

        val softwareSystemPageViewModel = SoftwareSystemPageViewModel(context, softwareSystem, SoftwareSystemPageViewModel.Tab.DECISIONS)
        val decisionTabViewModel = softwareSystemPageViewModel.createDecisionsTabViewModel(softwareSystem, SoftwareSystemPageViewModel.Tab.DECISIONS)

        assertThat(decisionTabViewModel.size).isEqualTo(1)
        assertThat(decisionTabViewModel.first().title).isEqualTo("Some Container")

    }
    @Test
    fun `container & system decision tabs available`() {
        val context = generatorContext()
        val softwareSystem = context.workspace.model.addSoftwareSystem("Software system")
        softwareSystem.documentation.addDecision(createDecision("1", "Accepted"))
        softwareSystem.addContainer("Some Container").documentation.addDecision(createDecision("2", "Proposed"))

        val softwareSystemPageViewModel = SoftwareSystemPageViewModel(context, softwareSystem, SoftwareSystemPageViewModel.Tab.DECISIONS)
        val decisionTabViewModel = softwareSystemPageViewModel.createDecisionsTabViewModel(softwareSystem, SoftwareSystemPageViewModel.Tab.DECISIONS)

        assertThat(decisionTabViewModel.size).isEqualTo(2)
        assertThat(decisionTabViewModel.first().title).isEqualTo("System")
        assertThat(decisionTabViewModel.last().title).isEqualTo("Some Container")
    }

    @Test
    fun `check container decisions tabs are filtered`() {
        val context = generatorContext()
        val softwareSystem = context.workspace.model.addSoftwareSystem("Software system")
        softwareSystem.addContainer("Some Container").documentation.addDecision(createDecision("2", "Proposed"))
        softwareSystem.addContainer("Another Container")

        val softwareSystemPageViewModel = SoftwareSystemPageViewModel(context, softwareSystem, SoftwareSystemPageViewModel.Tab.DECISIONS)
        val decisionTabViewModel = softwareSystemPageViewModel.createDecisionsTabViewModel(softwareSystem, SoftwareSystemPageViewModel.Tab.DECISIONS)

        assertThat(decisionTabViewModel.size).isEqualTo(1)
        assertThat(decisionTabViewModel.last().title).isEqualTo("Some Container")
    }
}
