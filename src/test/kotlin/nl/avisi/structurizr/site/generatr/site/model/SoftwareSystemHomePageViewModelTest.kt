package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import com.structurizr.documentation.Format
import com.structurizr.documentation.Section
import com.structurizr.model.SoftwareSystem
import kotlin.test.Test

class SoftwareSystemHomePageViewModelTest : ViewModelTest() {
    @Test
    fun `active tab`() {
        val generatorContext = generatorContext()
        val softwareSystem: SoftwareSystem = generatorContext.workspace.model.addSoftwareSystem("Software system")
        val viewModel = SoftwareSystemHomePageViewModel(generatorContext, softwareSystem)

        assertThat(viewModel.tabs.single { it.link.active }.tab)
            .isEqualTo(SoftwareSystemPageViewModel.Tab.HOME)
    }

    @Test
    fun `no section present`() {
        val generatorContext = generatorContext()
        val softwareSystem: SoftwareSystem = generatorContext.workspace.model.addSoftwareSystem("Software system")
            .apply { description = "It's a system." }
        val viewModel = SoftwareSystemHomePageViewModel(generatorContext, softwareSystem)

        assertThat(viewModel.content)
            .prop(MarkdownViewModel::markdown).isEqualTo(
                "# Description${System.lineSeparator()}${softwareSystem.description}"
            )
    }

    @Test
    fun `section present`() {
        val generatorContext = generatorContext()
        val softwareSystem: SoftwareSystem = generatorContext.workspace.model.addSoftwareSystem("Software system")
            .apply {
                documentation.addSection(Section("Title", Format.Markdown, "# Content"))
            }
        val viewModel = SoftwareSystemHomePageViewModel(generatorContext, softwareSystem)

        assertThat(viewModel.content)
            .prop(MarkdownViewModel::markdown).isEqualTo(
                softwareSystem.documentation.sections.single().content
            )
    }
}
