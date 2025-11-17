package nl.avisi.structurizr.site.generatr.site.e2e.decisions

import com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat
import nl.avisi.structurizr.site.generatr.site.e2e.E2ETestFixture
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import kotlin.test.Test

class SoftwareSystemContainerComponentDecisionsPageTest : E2ETestFixture() {
    private val decisionPageTestHelper = DecisionsPageTestHelper()

    @BeforeEach
    @Order(2)
    fun `navigate to page`() {
        page.navigate(SITE_URL)

        page.locator(".menu a").and(page.getByText("Internet Banking System")).click()
        page.locator(".container > .tabs a").and(page.getByText("Decisions")).click()
        page.locator(".content > .tabs a").and(page.getByText("API Application")).click()
        page.locator(".content > .tabs a").and(page.getByText("E-mail Component")).click()
    }

    @Test
    fun `title is correct`() {
        assertThat(page).hasTitle("Internet Banking System | Big Bank plc")
    }

    @Test
    fun `decision tabs render correctly`() {
        decisionPageTestHelper.testDecisionTabs(page) { tabs ->
            assertThat(tabs).hasCount(3)

            assertThat(tabs.nth(0)).containsText("System")
            assertThat(tabs.nth(1)).containsText("API Application")
            assertThat(tabs.nth(2)).containsText("Database")

            assertThat(tabs.nth(1)).hasClass("is-active")
        }
    }

    @Test
    fun `component decision tabs render correctly`() {
        decisionPageTestHelper.testComponentDecisionTabs(page) { tabs ->
            assertThat(tabs).hasCount(2)

            assertThat(tabs.nth(0)).containsText("Mainframe Banking System Facade")
            assertThat(tabs.nth(1)).containsText("E-mail Component")

            assertThat(tabs.nth(1)).hasClass("is-active")
        }
    }

    @Test
    fun `decisions table renders correctly`() {
        decisionPageTestHelper.testDecisionsTable(page) { locators ->
            val expectedData = listOf(
                listOf("1", "21-06-2022", "Accepted", "Record Email Component architecture decision"),
                listOf("2", "17-12-2024", "Superseded", "Implement Feature 1"),
                listOf("3", "17-12-2024", "Accepted", "Another Realisation of Feature 1"),
            )

            expectedData.forEachIndexed { rowIndex, row ->
                row.forEachIndexed { colIndex, text ->
                    assertThat(locators[rowIndex].nth(colIndex)).containsText(text)
                }
            }
        }
    }
}
