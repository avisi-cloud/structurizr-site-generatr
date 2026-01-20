package nl.avisi.structurizr.site.generatr.site.e2e.decisions

import com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat
import nl.avisi.structurizr.site.generatr.site.e2e.E2ETestFixture
import nl.avisi.structurizr.site.generatr.site.e2e.PageTestHelper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import kotlin.test.Test

class SoftwareSystemDecisionsPageTest : E2ETestFixture() {
    private val pageTestHelper = PageTestHelper()
    private val decisionPageTestHelper = DecisionsPageTestHelper()

    @BeforeEach
    @Order(2)
    fun `navigate to page`() {
        page.navigate(SITE_URL)

        page.locator(".menu a").and(page.getByText("Internet Banking System")).click()
        page.locator(".container > .tabs a").and(page.getByText("Decisions")).click()
    }

    @Test
    fun `title is correct`() {
        assertThat(page).hasTitle("Internet Banking System | Big Bank plc")
    }

    @Test
    fun `menu renders correctly`() {
        pageTestHelper.testMenu(page) { menu ->
            assertThat(menu.locator(".is-active")).containsText("Internet Banking System")
        }
    }

    @Test
    fun `title and subtitle render correctly`() {
        pageTestHelper.testTitleAndSubtitle(
            page,
            "Internet Banking System",
            "Allows customers to view information about their bank accounts, and make payments."
        )
    }

    @Test
    fun `tabs render correctly`() {
        pageTestHelper.testTabs(page) { tabs ->
            assertThat(tabs.and(page.locator(".is-active"))).containsText("Decisions")
        }
    }

    @Test
    fun `decision tabs render correctly`() {
        decisionPageTestHelper.testDecisionTabs(page) { tabs ->
            assertThat(tabs).hasCount(3)

            assertThat(tabs.nth(0)).containsText("System")
            assertThat(tabs.nth(1)).containsText("API Application")
            assertThat(tabs.nth(2)).containsText("Database")

            assertThat(tabs.nth(0)).containsClass("is-active")
        }
    }

    @Test
    fun `component decision tabs render correctly`() {
        decisionPageTestHelper.testComponentDecisionTabs(page) { locators ->
            assertThat(locators).hasCount(0)
        }
    }

    @Test
    fun `decisions table renders correctly`() {
        decisionPageTestHelper.testDecisionsTable(page) { locators ->
            val expectedData = listOf(
                listOf("1", "21-06-2022", "Accepted", "Record Internet Banking System architecture decisions")
            )

            expectedData.forEachIndexed { rowIndex, row ->
                row.forEachIndexed { colIndex, text ->
                    assertThat(locators[rowIndex].nth(colIndex)).containsText(text)
                }
            }
        }
    }
}
