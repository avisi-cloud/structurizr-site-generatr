package nl.avisi.structurizr.site.generatr.site.e2e.decisions

import assertk.assertThat
import assertk.assertions.isEqualTo
import nl.avisi.structurizr.site.generatr.site.e2e.E2ETestFixture
import nl.avisi.structurizr.site.generatr.site.e2e.PageTestHelper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import kotlin.test.Test

class SoftwareSystemContainerComponentDecisionPageTest : E2ETestFixture() {
    private val pageTestHelper = PageTestHelper()
    private val decisionPageTestHelper = DecisionPageTestHelper()

    @BeforeEach
    @Order(2)
    fun `navigate to page`() {
        page.navigate(SITE_URL)

        page.locator(".menu a").and(page.getByText("Internet Banking System")).click()
        page.locator(".container > .tabs a").and(page.getByText("Decisions")).click()
        page.locator(".content > .tabs a").and(page.getByText("API Application")).click()
        page.locator(".content > .tabs a").and(page.getByText("E-mail Component")).click()
        page.locator(".table a").and(page.getByText("Record Email Component architecture decision")).click()
    }

    @Test
    fun `title is correct`() {
        assertThat(page.title()).isEqualTo("Internet Banking System | Big Bank plc")
    }

    @Test
    fun `menu renders correctly`() {
        pageTestHelper.testMenu(page) { menu ->
            assertThat(menu.locator(".is-active").innerText()).isEqualTo("Internet Banking System")
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
            assertThat(tabs.and(page.locator(".is-active")).innerText()).isEqualTo("Decisions")
        }
    }

    @Test
    fun `content renders correctly`() {
        decisionPageTestHelper.testDecisionContent(page) { data ->
            assertThat(data).isEqualTo(listOf(
                "1. Record Email Component architecture decision",
                "Date: 2022-06-21",
                "Accepted",
                "We need to record the architectural decisions made on this project.",
                "We will use Architecture Decision Records, as described by Michael Nygard.",
                "See Michael Nygard’s article, linked above. For a lightweight ADR toolset, see Nat Pryce’s adr-tools."
            ))
        }
    }
}
