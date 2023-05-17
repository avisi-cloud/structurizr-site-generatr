package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.isEqualTo
import java.time.LocalDate
import java.time.Month
import kotlin.test.Test

class DecisionTabViewModelTest : ViewModelTest() {

    @Test
    fun `no decision tabs`() {
        val context = generatorContext()
        val softwareSystem = context.workspace.model.addSoftwareSystem("Software system")

        val softwareSystemPageViewModel = SoftwareSystemPageViewModel(context, softwareSystem, SoftwareSystemPageViewModel.Tab.DECISIONS)
        val decisionTabViewModel = softwareSystemPageViewModel.createDecisionsTabViewModel(softwareSystem, SoftwareSystemPageViewModel.Tab.DECISIONS)

        assertThat(decisionTabViewModel.size).isEqualTo(1)
        assertThat(decisionTabViewModel[0].visible).isEqualTo(false)
    }

    @Test
    fun `software system decision tab available`() {
        val context = generatorContext()
        val softwareSystem = context.workspace.model.addSoftwareSystem("Software system")
        softwareSystem.documentation.addDecision(createDecision("1", "Accepted"))

        val softwareSystemPageViewModel = SoftwareSystemPageViewModel(context, softwareSystem, SoftwareSystemPageViewModel.Tab.DECISIONS)
        val decisionTabViewModel = softwareSystemPageViewModel.createDecisionsTabViewModel(softwareSystem, SoftwareSystemPageViewModel.Tab.DECISIONS)

        assertThat(decisionTabViewModel.size).isEqualTo(1)
        assertThat(decisionTabViewModel[0].visible).isEqualTo(true)
    }

    @Test
    fun `container decision tab available `() {
        val context = generatorContext()
        val softwareSystem = context.workspace.model.addSoftwareSystem("Software system")
        softwareSystem.addContainer("Some Container").documentation.addDecision(createDecision("2", "Proposed"))

        val softwareSystemPageViewModel = SoftwareSystemPageViewModel(context, softwareSystem, SoftwareSystemPageViewModel.Tab.DECISIONS)
        val decisionTabViewModel = softwareSystemPageViewModel.createDecisionsTabViewModel(softwareSystem, SoftwareSystemPageViewModel.Tab.DECISIONS)

        assertThat(decisionTabViewModel.size).isEqualTo(2)
        assertThat(decisionTabViewModel[1].visible).isEqualTo(true)
    }
    @Test
    fun `container & system decision tabs available `() {
        val context = generatorContext()
        val softwareSystem = context.workspace.model.addSoftwareSystem("Software system")
        softwareSystem.documentation.addDecision(createDecision("1", "Accepted"))
        softwareSystem.addContainer("Some Container").documentation.addDecision(createDecision("2", "Proposed"))

        val softwareSystemPageViewModel = SoftwareSystemPageViewModel(context, softwareSystem, SoftwareSystemPageViewModel.Tab.DECISIONS)
        val decisionTabViewModel = softwareSystemPageViewModel.createDecisionsTabViewModel(softwareSystem, SoftwareSystemPageViewModel.Tab.DECISIONS)

        assertThat(decisionTabViewModel.size).isEqualTo(2)
        assertThat(decisionTabViewModel.all { it.visible }).isEqualTo(true)
    }
}
