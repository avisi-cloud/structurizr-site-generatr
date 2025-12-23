package nl.avisi.structurizr.site.generatr.site.e2e.decisions

import assertk.assertThat
import assertk.assertions.isEqualTo
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
    fun `decision tabs render correctly`() {
        decisionPageTestHelper.testDecisionTabs(page) { activeTab, tabs ->
            assertThat(tabs.size).isEqualTo(3)

            assertThat(tabs[0]).isEqualTo("System")
            assertThat(tabs[1]).isEqualTo("API Application")
            assertThat(tabs[2]).isEqualTo("Database")

            assertThat(tabs[1], activeTab)
        }
    }

    @Test
    fun `component decision tabs render correctly`() {
        decisionPageTestHelper.testComponentDecisionTabs(page) { tabs, data ->
            assertThat(data.size).isEqualTo(2)

            assertThat(data[0]).isEqualTo("Mainframe Banking System Facade")
            assertThat(data[1]).isEqualTo("E-mail Component")

            assertThat(tabs.and(page.locator(".is-active")).innerText()).isEqualTo("E-mail Component")
        }
    }

    @Test
    fun `decisions table renders correctly`() {
        decisionPageTestHelper.testDecisionsTable(page) { data ->
            assertThat(data.size).isEqualTo(3)

            assertThat(data[0]).isEqualTo(listOf("1", "21-06-2022", "Accepted", "Record Email Component architecture decision"))
            assertThat(data[1]).isEqualTo(listOf("2", "17-12-2024", "Superseded", "Implement Feature 1"))
            assertThat(data[2]).isEqualTo(listOf("3", "17-12-2024", "Accepted", "Another Realisation of Feature 1"))
        }
    }
}
