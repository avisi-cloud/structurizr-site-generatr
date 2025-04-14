package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.structurizr.documentation.Format
import nl.avisi.structurizr.site.generatr.normalize
import kotlin.test.Test

class SoftwareSystemContainerComponentSectionPageViewModelTest : ViewModelTest() {
    private val generatorContext = generatorContext()
    private val softwareSystem = generatorContext.workspace.model.addSoftwareSystem("Software system").also {
        it.addContainer("API Application").apply {
            documentation.addSection(createSection())

            addComponent("Some Component").apply {
                documentation.addSection(createSection())
            }
        }
    }

    private val component = softwareSystem.containers.first().components.first()
    private val section = component.documentation.sections.first()

    @Test
    fun url() {
        val viewModel = SoftwareSystemContainerComponentSectionPageViewModel(generatorContext, component, section)

        assertThat(SoftwareSystemContainerComponentSectionPageViewModel.url(component, section))
            .isEqualTo("/software-system/sections/api-application/some-component/content")
        assertThat(viewModel.url)
            .isEqualTo(SoftwareSystemContainerComponentSectionPageViewModel.url(component, section))
    }

    @Test
    fun `active tab`() {
        val viewModel = SoftwareSystemContainerComponentSectionPageViewModel(generatorContext, component, section)
        assertThat(viewModel.tabs.single { it.link.active }.tab)
            .isEqualTo(SoftwareSystemPageViewModel.Tab.SECTIONS)
    }

    @Test
    fun content() {
        val viewModel = SoftwareSystemContainerComponentSectionPageViewModel(generatorContext, component, section)

        assertThat(viewModel.content).isEqualTo(toHtml(viewModel, section.content, Format.Markdown, svgFactory))
    }
}
