package nl.avisi.structurizr.site.generatr.site.model

import assertk.all
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import com.structurizr.documentation.Format
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
            .isEqualTo(
                toHtml(
                    viewModel,
                    "# Description${System.lineSeparator()}${softwareSystem.description}",
                    Format.Markdown,
                    svgFactory
                )
            )
    }

    @Test
    fun `section present`() {
        val generatorContext = generatorContext()
        val softwareSystem: SoftwareSystem = generatorContext.workspace.model.addSoftwareSystem("Software system")
            .apply {
                documentation.addSection(createSection("# Title"))
            }
        val viewModel = SoftwareSystemHomePageViewModel(generatorContext, softwareSystem)

        assertThat(viewModel.content)
            .isEqualTo(
                toHtml(viewModel, softwareSystem.documentation.sections.single().content, Format.Markdown, svgFactory)
            )
    }

    @Test
    fun `no properties present`() {
        val generatorContext = generatorContext()
        val softwareSystem: SoftwareSystem = generatorContext.workspace.model.addSoftwareSystem("Software system")
            .apply { description = "It's a system." }
        val viewModel = SoftwareSystemHomePageViewModel(generatorContext, softwareSystem)

        assertThat(viewModel)
            .all {
                prop(SoftwareSystemHomePageViewModel::hasProperties).isEqualTo(false)
                prop(SoftwareSystemHomePageViewModel::propertiesTable)
                    .isEqualTo(createPropertiesTableViewModel(mapOf()))
            }
    }

    @Test
    fun `properties present`() {
        val generatorContext = generatorContext()
        val softwareSystem: SoftwareSystem = generatorContext.workspace.model.addSoftwareSystem("Software system")
            .apply {
                description = "It's a system."
                addProperty("Url", "https://tempuri.org/")
            }
        val viewModel = SoftwareSystemHomePageViewModel(generatorContext, softwareSystem)

        assertThat(viewModel)
            .all {
                prop(SoftwareSystemHomePageViewModel::hasProperties).isEqualTo(true)
                prop(SoftwareSystemHomePageViewModel::propertiesTable)
                    .isEqualTo(createPropertiesTableViewModel(softwareSystem.properties))
            }
    }

    @Test
    fun `internal properties present`() {
        val generatorContext = generatorContext()
        val softwareSystem: SoftwareSystem = generatorContext.workspace.model.addSoftwareSystem("Software system")
            .apply {
                description = "It's a system."
                addProperty("structurizr.dsl.identifier", "id")
            }
        val viewModel = SoftwareSystemHomePageViewModel(generatorContext, softwareSystem)

        assertThat(viewModel)
            .all {
                prop(SoftwareSystemHomePageViewModel::hasProperties).isEqualTo(false)
                prop(SoftwareSystemHomePageViewModel::propertiesTable)
                    .isEqualTo(createPropertiesTableViewModel(mapOf()))
            }
    }
}
