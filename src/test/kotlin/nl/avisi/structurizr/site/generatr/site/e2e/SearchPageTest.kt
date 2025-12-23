package nl.avisi.structurizr.site.generatr.site.e2e

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.microsoft.playwright.Locator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import kotlin.test.Test

class SearchPageTest : E2ETestFixture() {
    private val pageTestHelper = PageTestHelper()

    @BeforeEach
    @Order(2)
    fun `navigate to page`() {
        page.navigate(SITE_URL)

        val search = page.locator(".navbar input").and(page.getByPlaceholder("Search..."))

        search.fill("internet banking system")
        search.press("Enter")
    }

    @Test
    fun `menu renders correctly`() {
        pageTestHelper.testMenu(page) { menu ->
            assertThat(menu.and(page.locator(".is-active")).count()).isEqualTo(0)
        }
    }

    @Test
    fun `content renders correctly`() {
        val content = page.locator(".content > *").filter(Locator.FilterOptions().setVisible(true))

        assertThat(content.nth(0).innerText()).isEqualTo("Search results")

        val searchResults = content.nth(1).locator("p:not(.mb-0)").also {
            it.nth(0).waitFor()
        }.all().map { it.locator("> *").all().map { it.innerText() } }

        assertThat(searchResults[0]).isEqualTo(listOf("Internet Banking System | Context views", "Context views"))
        assertThat(searchResults[1]).isEqualTo(listOf("E-mail System | Dependencies", "Dependencies"))
        assertThat(searchResults[2]).isEqualTo(listOf("Internet Banking System | Description", "Software System Info"))
        assertThat(searchResults[3]).isEqualTo(listOf("Mainframe Banking System | Dependencies", "Dependencies"))
        assertThat(searchResults[4]).isEqualTo(listOf("Internet Banking System | Component views", "Component views"))
        assertThat(searchResults[5]).isEqualTo(listOf("Internet Banking System | Container views", "Container views"))
        assertThat(searchResults[6]).isEqualTo(listOf("Internet Banking System | Record Internet Banking System architecture decisions", "Software System Decision"))
        assertThat(searchResults[7]).isEqualTo(listOf("ATM | Dependencies", "Dependencies"))
        assertThat(searchResults[8]).isEqualTo(listOf("Mainframe Banking System | Context views", "Context views"))
        assertThat(searchResults[9]).isEqualTo(listOf("Internet Banking System | Dependencies", "Dependencies"))
        assertThat(searchResults[10]).isEqualTo(listOf("E-mail System | Context views", "Context views"))
        assertThat(searchResults[11]).isEqualTo(listOf("Big Bank plc architecture", "Home"))
        assertThat(searchResults[12]).isEqualTo(listOf("Embedding diagrams and images", "Workspace Documentation"))
    }
}
