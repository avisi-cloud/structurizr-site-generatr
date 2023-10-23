package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.structurizr.documentation.Format
import nl.avisi.structurizr.site.generatr.normalize
import kotlin.test.Test

class SoftwareSystemSectionPageViewModelTest : ViewModelTest() {
    private val generatorContext = generatorContext()
    private val softwareSystem = generatorContext.workspace.model.addSoftwareSystem("Software System")

    @Test
    fun url() {
        val section = createSection()
        val viewModel = SoftwareSystemSectionPageViewModel(generatorContext, softwareSystem, section)

        assertThat(SoftwareSystemSectionPageViewModel.url(softwareSystem, section))
            .isEqualTo("/${softwareSystem.name.normalize()}/sections/${section.order}")
        assertThat(viewModel.url)
            .isEqualTo(SoftwareSystemSectionPageViewModel.url(softwareSystem, section))
    }

    @Test
    fun `active tab`() {
        val viewModel = SoftwareSystemSectionPageViewModel(generatorContext, softwareSystem, createSection())

        assertThat(viewModel.tabs.single { it.link.active }.tab)
            .isEqualTo(SoftwareSystemPageViewModel.Tab.SECTIONS)
    }

    @Test
    fun content() {
        val section = createSection()
        val viewModel = SoftwareSystemSectionPageViewModel(generatorContext, softwareSystem, section)

        assertThat(viewModel.content).isEqualTo(toHtml(viewModel, section.content, Format.Markdown, svgFactory))
    }
}
