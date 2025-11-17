package nl.avisi.structurizr.site.generatr.site.e2e

import com.microsoft.playwright.Locator
import com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat
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
    fun `title is correct`() {
        assertThat(page).hasTitle("Search results | Big Bank plc")
    }

    @Test
    fun `menu renders correctly`() {
        pageTestHelper.testMenu(page) { menu ->
            assertThat(menu.and(page.locator(".is-active"))).hasCount(0)
        }
    }

    @Test
    fun `content renders correctly`() {
        val content = page.locator(".content > *").filter(Locator.FilterOptions().setVisible(true))

        assertThat(content.nth(0)).containsText("Search results")

        val expectedSearchResults = listOf(
            listOf("Internet Banking System | Context views", "Context views"),
            listOf("E-mail System | Dependencies", "Dependencies"),
            listOf("Internet Banking System | Description", "Software System Info"),
            listOf("Mainframe Banking System | Dependencies", "Dependencies"),
            listOf("Internet Banking System | Component views", "Component views"),
            listOf("Internet Banking System | Container views", "Container views"),
            listOf("Internet Banking System | Record Internet Banking System architecture decisions", "Software System Decision"),
            listOf("ATM | Dependencies", "Dependencies"),
            listOf("Mainframe Banking System | Context views", "Context views"),
            listOf("Internet Banking System | Dependencies", "Dependencies"),
            listOf("E-mail System | Context views", "Context views"),
            listOf("Big Bank plc architecture", "Home"),
            listOf("Embedding diagrams and images", "Workspace Documentation"),
        )

        expectedSearchResults.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { colIndex, text ->
                assertThat(content.nth(1).locator("p:not(.mb-0)").also {
                    it.nth(0).waitFor()
                }.nth(rowIndex).locator("> *").nth(colIndex)).containsText(text)
            }
        }
    }
}
