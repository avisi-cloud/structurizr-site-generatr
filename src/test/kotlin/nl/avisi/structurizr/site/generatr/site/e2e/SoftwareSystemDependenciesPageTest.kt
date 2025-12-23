package nl.avisi.structurizr.site.generatr.site.e2e

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import kotlin.test.Test

class SoftwareSystemDependenciesPageTest : E2ETestFixture() {
    private val pageTestHelper = PageTestHelper()

    @BeforeEach
    @Order(2)
    fun `navigate to page`() {
        page.navigate(SITE_URL)

        page.locator(".menu a").and(page.getByText("Internet Banking System")).click()
        page.locator(".container > .tabs a").and(page.getByText("Dependencies")).click()
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
            assertThat(tabs.and(page.locator(".is-active")).innerText()).isEqualTo("Dependencies")
        }
    }

    @Test
    fun `content renders correctly`() {
        val content = page.locator(".content > *")

        assertThat(content.nth(0).innerText()).isEqualTo("Inbound")
        assertThat(content.nth(2).innerText()).isEqualTo("Outbound")

        val expectedTableHeadings = listOf("System", "Description", "Technology")

        assertThat(content.nth(1).locator("thead th").all().map { it.innerText() }).isEqualTo(expectedTableHeadings)
        assertThat(content.nth(3).locator("thead th").all().map { it.innerText() }).isEqualTo(expectedTableHeadings)

        val outboundData = content.nth(3).locator("tbody tr").all().map { it.locator("td").all().map { it.innerText() } }

        assertThat(outboundData[0]).isEqualTo(listOf("E-mail System", "Sends e-mail using", ""))
        assertThat(outboundData[1]).isEqualTo(listOf("Mainframe Banking System", "Gets account information from, and makes payments using", ""))
    }
}
