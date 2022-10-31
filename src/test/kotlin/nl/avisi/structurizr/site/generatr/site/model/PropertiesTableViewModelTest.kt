package nl.avisi.structurizr.site.generatr.site.model

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test

class PropertiesTableViewModelTest : ViewModelTest() {

    @Test
    fun `no properties available`() {
        assertThat(createPropertiesTableViewModel(emptyMap()))
            .isEqualTo(
                TableViewModel.create {
                    propertiesTableHeaderRow()
                }
            )
    }

    @Test
    fun `many properties sorted by key`() {
        val properties = mapOf(
            "b name" to "value",
            "a name" to "value"
        )

        assertThat(createPropertiesTableViewModel(properties)).isEqualTo(
            TableViewModel.create {
                propertiesTableHeaderRow()
                bodyRow(
                    cell("a name"),
                    cell("value"),
                )
                bodyRow(
                    cell("b name"),
                    cell("value"),
                )
            }
        )
    }

    @Test
    fun `properties with link`() {
        val properties = mapOf(
            "url" to "https://tempuri.org/",
        )

        assertThat(createPropertiesTableViewModel(properties)).isEqualTo(
            TableViewModel.create {
                propertiesTableHeaderRow()
                bodyRow(
                    cell("url"),
                    cellWithExternalLink("https://tempuri.org/", "https://tempuri.org/"),
                )
            }
        )
    }

    private fun TableViewModel.TableViewInitializerContext.propertiesTableHeaderRow() {
        headerRow(headerCell("Name"), headerCell("Value"))
    }
}
