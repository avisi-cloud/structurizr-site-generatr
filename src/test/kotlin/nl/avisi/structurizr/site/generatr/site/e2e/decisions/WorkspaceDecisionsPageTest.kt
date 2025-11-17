package nl.avisi.structurizr.site.generatr.site.e2e.decisions

import com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat
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
        assertThat(page).hasTitle("Decisions | Big Bank plc")
    }

    @Test
    fun `menu renders correctly`() {
        pageTestHelper.testMenu(page) { menu ->
            assertThat(menu.locator(".is-active")).containsText("Decisions")
        }
    }

    @Test
    fun `decisions table renders correctly`() {
        decisionsPageTestHelper.testDecisionsTable(page) { locators ->
            val expectedData = listOf(
                listOf("1", "21-06-2022", "Accepted", "Record architecture decisions"),
                listOf("2", "17-12-2024", "Superseded", "Implement Feature 1"),
                listOf("3", "17-12-2024", "Accepted", "Another Realisation of Feature 1"),
                listOf("4", "13-11-2025", "Accepted", "Using Oracle database schema"),
            )

            expectedData.forEachIndexed { rowIndex, row ->
                row.forEachIndexed { colIndex, text ->
                    assertThat(locators[rowIndex].nth(colIndex)).containsText(text)
                }
            }
        }
    }
}
