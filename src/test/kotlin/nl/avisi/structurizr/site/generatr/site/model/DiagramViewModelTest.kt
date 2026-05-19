package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.*
import kotlin.test.Test

class DiagramViewModelTest : ViewModelTest() {

    @Test
    fun `diagram without legend has no legend fields`() {
        val viewModel = DiagramViewModel.forView(
            pageViewModel(), "key-1", "Name", "Title", "Description", svgFactory
        )

        assertThat(viewModel.hasLegend).isFalse()
        assertThat(viewModel.legendSvg).isNull()
        assertThat(viewModel.legendWidthInPixels).isNull()
        assertThat(viewModel.legendSvgLocation).isNull()
        assertThat(viewModel.legendPngLocation).isNull()
        assertThat(viewModel.legendPumlLocation).isNull()
    }

    @Test
    fun `diagram with legend has legend fields populated`() {
        val legendSvgFactory = { _: String -> """<svg viewBox="0 0 400 300"></svg>""" }

        val viewModel = DiagramViewModel.forView(
            pageViewModel(), "key-1", "Name", "Title", "Description", svgFactory, legendSvgFactory
        )

        assertThat(viewModel.hasLegend).isTrue()
        assertThat(viewModel.legendSvg).isEqualTo("""<svg viewBox="0 0 400 300"></svg>""")
        assertThat(viewModel.legendWidthInPixels).isEqualTo(400)
        assertThat(viewModel.legendSvgLocation).isNotNull()
        assertThat(viewModel.legendSvgLocation!!.relativeHref).contains("/svg/key-1.legend.svg")
        assertThat(viewModel.legendPngLocation).isNotNull()
        assertThat(viewModel.legendPngLocation!!.relativeHref).contains("/png/key-1.legend.png")
        assertThat(viewModel.legendPumlLocation).isNotNull()
        assertThat(viewModel.legendPumlLocation!!.relativeHref).contains("/puml/key-1.legend.puml")
    }

    @Test
    fun `diagram with null legend factory has no legend`() {
        val legendSvgFactory = { _: String -> null as String? }

        val viewModel = DiagramViewModel.forView(
            pageViewModel(), "key-1", "Name", "Title", "Description", svgFactory, legendSvgFactory
        )

        assertThat(viewModel.hasLegend).isFalse()
    }

    @Test
    fun `legend width extracted from viewBox`() {
        val legendSvgFactory = { _: String -> """<svg viewBox="0 0 600 200"></svg>""" }

        val viewModel = DiagramViewModel.forView(
            pageViewModel(), "key-1", "Name", "Title", "Description", svgFactory, legendSvgFactory
        )

        assertThat(viewModel.legendWidthInPixels).isEqualTo(600)
    }
}
