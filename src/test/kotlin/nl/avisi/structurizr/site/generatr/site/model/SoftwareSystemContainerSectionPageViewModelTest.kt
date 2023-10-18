package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.isEqualTo
import nl.avisi.structurizr.site.generatr.normalize
import kotlin.test.Test

class SoftwareSystemContainerSectionPageViewModelTest : ViewModelTest() {
    private val generatorContext = generatorContext()
    private val softwareSystem = generatorContext.workspace.model.addSoftwareSystem("Software System").also {
        it.addContainer("API Application")
    }
    private val container = softwareSystem.containers.first()

    @Test
    fun url() {
        val section = createSection()
        val viewModel = SoftwareSystemContainerSectionPageViewModel(generatorContext, container, section)

        assertThat(SoftwareSystemContainerSectionPageViewModel.url(container, section))
            .isEqualTo("/${softwareSystem.name.normalize()}/sections/${container.name.normalize()}/${section.order}")
        assertThat(viewModel.url)
            .isEqualTo(SoftwareSystemContainerSectionPageViewModel.url(container, section))
    }

    @Test
    fun `active tab`() {
        val viewModel = SoftwareSystemContainerSectionPageViewModel(generatorContext, container, createSection())

        assertThat(viewModel.tabs.single { it.link.active }.tab)
            .isEqualTo(SoftwareSystemPageViewModel.Tab.SECTIONS)
    }

    @Test
    fun content() {
        val section = createSection()
        val viewModel = SoftwareSystemContainerSectionPageViewModel(generatorContext, container, section)

        assertThat(viewModel.content).isEqualTo(markdownToHtml(viewModel, section.content, svgFactory))
    }
}
