package nl.avisi.structurizr.site.generatr.site.e2e.decisions

import assertk.assertThat
import assertk.assertions.isEqualTo
import nl.avisi.structurizr.site.generatr.site.e2e.E2ETestFixture
import nl.avisi.structurizr.site.generatr.site.e2e.PageTestHelper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import kotlin.test.Test

class WorkspaceDecisionsPageTest : E2ETestFixture() {
    private val pageTestHelper = PageTestHelper()
    private val decisionsPageTestHelper = DecisionsPageTestHelper()

    @BeforeEach
    @Order(2)
    fun `navigate to page`() {
        page.navigate(SITE_URL)

        page.locator(".menu a").and(page.getByText("Decisions")).click()
    }

    @Test
    fun `title is correct`() {
        assertThat(page.title()).isEqualTo("Decisions | Big Bank plc")
    }

    @Test
    fun `menu renders correctly`() {
        pageTestHelper.testMenu(page) { menu ->
            assertThat(menu.locator(".is-active").innerText()).isEqualTo("Decisions")
        }
    }

    @Test
    fun `decisions table renders correctly`() {
        decisionsPageTestHelper.testDecisionsTable(page) { data ->
            assertThat(data.size).isEqualTo(4)

            assertThat(data[0]).isEqualTo(listOf("1", "21-06-2022", "Accepted", "Record architecture decisions"))
            assertThat(data[1]).isEqualTo(listOf("2", "17-12-2024", "Superseded", "Implement Feature 1"))
            assertThat(data[2]).isEqualTo(listOf("3", "17-12-2024", "Accepted", "Another Realisation of Feature 1"))
            assertThat(data[3]).isEqualTo(listOf("4", "13-11-2025", "Accepted", "Using Oracle database schema"))
        }
    }
}
