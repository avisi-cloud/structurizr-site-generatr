package nl.avisi.structurizr.site.generatr.site.e2e

import assertk.assertThat
import assertk.assertions.isEqualTo
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
        page.onDOMContentLoaded {
            assertThat(page.title()).isEqualTo("Software Systems | Big Bank plc")
        }
    }

    @Test
    fun `menu renders correctly`() {
        pageTestHelper.testMenu(page) { menu ->
            assertThat(menu.locator(".is-active").innerText()).isEqualTo("Software Systems")
        }
    }

    @Test
    fun `content renders correctly`() {
        val content = page.locator(".content > *")

        val title = content.nth(0).innerText()

        assertThat(title).isEqualTo("Software Systems")

        val table = content.and(page.locator(".table"))

        val tableHeadings = table.locator("thead th").all().map { it.innerText() }

        assertThat(tableHeadings[0]).isEqualTo("Name")
        assertThat(tableHeadings[1]).isEqualTo("Description")

        val data = table.locator("tbody tr").all().map { row -> row.locator("td").all().map { it.innerText() } }

        assertThat(data[0]).isEqualTo(listOf("Acquirer (External)", "Facilitates PIN transactions for merchants."))
        assertThat(data[1]).isEqualTo(listOf("ATM", "Allows customers to withdraw cash."))
        assertThat(data[2]).isEqualTo(listOf("E-mail System", "The internal Microsoft Exchange e-mail system."))
        assertThat(data[3]).isEqualTo(listOf("Internet Banking System", "Allows customers to view information about their bank accounts, and make payments."))
        assertThat(data[4]).isEqualTo(listOf("Mainframe Banking System", "Stores all of the core banking information about customers, accounts, transactions, etc."))
    }
}
