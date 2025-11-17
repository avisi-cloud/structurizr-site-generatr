package nl.avisi.structurizr.site.generatr.site.e2e.decisions

import com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat
import nl.avisi.structurizr.site.generatr.site.e2e.E2ETestFixture
import nl.avisi.structurizr.site.generatr.site.e2e.PageTestHelper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import kotlin.test.Test

class SoftwareSystemContainerDecisionPage : E2ETestFixture() {
    private val pageTestHelper = PageTestHelper()
    private val decisionPageTestHelper = DecisionPageTestHelper()

    @BeforeEach
    @Order(2)
    fun `navigate to page`() {
        page.navigate(SITE_URL)

        page.locator(".menu a").and(page.getByText("Internet Banking System")).click()
        page.locator(".container > .tabs a").and(page.getByText("Decisions")).click()
        page.locator(".content > .tabs a").and(page.getByText("Database")).click()
        page.locator(".table a").and(page.getByText("Using Oracle database schema")).click()
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
    fun `content renders correctly`() {
        decisionPageTestHelper.testDecisionContent(page) { locators ->
            val expected = listOf(
                "4. Using Oracle database schema",
                "Date: 2025-11-13",
                "Accepted",
                "The issue motivating this decision, and any context that influences or constrains the decision.",
                "The change that we’re proposing or have agreed to implement.",
                "What becomes easier or more difficult to do and any risks introduced by the change that will need to be mitigated."
            )

            expected.forEachIndexed { index, text ->
                assertThat(locators[index]).containsText(text)
            }
        }
    }
}
