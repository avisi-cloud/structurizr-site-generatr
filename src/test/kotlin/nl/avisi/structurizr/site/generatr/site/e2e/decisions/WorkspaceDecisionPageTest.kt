package nl.avisi.structurizr.site.generatr.site.e2e.decisions

import com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat
import nl.avisi.structurizr.site.generatr.site.e2e.E2ETestFixture
import nl.avisi.structurizr.site.generatr.site.e2e.PageTestHelper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import kotlin.test.Test

class WorkspaceDecisionPageTest : E2ETestFixture() {
    private val pageTestHelper = PageTestHelper()
    private val decisionPageTestHelper = DecisionPageTestHelper()

    @BeforeEach
    @Order(2)
    fun `navigate to page`() {
        page.navigate(SITE_URL)

        page.locator(".menu a").and(page.getByText("Decisions")).click()
        page.locator(".content a").and(page.getByText("Record architecture decisions")).click()
    }

    @Test
    fun `title is correct`() {
        page.onDOMContentLoaded {
            assertThat(page).hasTitle("Record architecture decisions | Big Bank plc")
        }
    }

    @Test
    fun `menu renders correctly`() {
        pageTestHelper.testMenu(page) { menu ->
            assertThat(menu.locator(".is-active")).containsText("Decisions")
        }
    }

    @Test
    fun `content renders correctly`() {
        decisionPageTestHelper.testDecisionContent(page) { locators ->
            val expectedData = listOf(
                "1. Record architecture decisions",
                "Date: 2022-06-21",
                "Accepted",
                "We need to record the architectural decisions made on this project.",
                "We will use Architecture Decision Records, as described by Michael Nygard.",
                "See Michael Nygard’s article, linked above. For a lightweight ADR toolset, see Nat Pryce’s adr-tools."
            )

            expectedData.forEachIndexed { index, text ->
                assertThat(locators[index]).containsText(text)
            }
        }
    }
}
