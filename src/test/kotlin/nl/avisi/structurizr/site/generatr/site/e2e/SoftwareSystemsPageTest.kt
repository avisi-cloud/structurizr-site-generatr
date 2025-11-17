package nl.avisi.structurizr.site.generatr.site.e2e

import com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import kotlin.test.Test

class SoftwareSystemsPageTest : E2ETestFixture() {
    private val pageTestHelper = PageTestHelper()

    @BeforeEach
    @Order(2)
    fun `navigate to page`() {
        page.navigate(SITE_URL)

        page.locator(".menu a").and(page.getByText("Software Systems")).click()
    }

    @Test
    fun `title is correct`() {
        assertThat(page).hasTitle("Software Systems | Big Bank plc")
    }

    @Test
    fun `menu renders correctly`() {
        pageTestHelper.testMenu(page) { menu ->
            assertThat(menu.locator(".is-active")).containsText("Software Systems")
        }
    }

    @Test
    fun `content renders correctly`() {
        val content = page.locator(".content > *")

        val title = content.nth(0)

        assertThat(title).containsText("Software Systems")

        val table = content.and(page.locator(".table"))

        val tableHeadings = table.locator("thead th")

        assertThat(tableHeadings.nth(0)).containsText("Name")
        assertThat(tableHeadings.nth(1)).containsText("Description")

        val expectedData = listOf(
            listOf(
                "Acquirer (External)",
                "Facilitates PIN transactions for merchants."
            ),
            listOf(
                "ATM",
                "Allows customers to withdraw cash."
            ),
            listOf(
                "E-mail System",
                "The internal Microsoft Exchange e-mail system."
            ),
            listOf(
                "Internet Banking System",
                "Allows customers to view information about their bank accounts, and make payments."
            ),
            listOf(
                "Mainframe Banking System",
                "Stores all of the core banking information about customers, accounts, transactions, etc."
            )
        )

        expectedData.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { colIndex, text ->
                assertThat(table.locator("tbody tr").nth(rowIndex).locator("td").nth(colIndex)).containsText(text)
            }
        }
    }
}
