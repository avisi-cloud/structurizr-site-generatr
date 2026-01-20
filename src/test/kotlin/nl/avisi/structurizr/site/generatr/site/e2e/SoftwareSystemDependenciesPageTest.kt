package nl.avisi.structurizr.site.generatr.site.e2e

import com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat
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
            assertThat(tabs.and(page.locator(".is-active"))).containsText("Dependencies")
        }
    }

    @Test
    fun `content renders correctly`() {
        val content = page.locator(".content > *")

        assertThat(content.nth(0)).containsText("Inbound")
        assertThat(content.nth(2)).containsText("Outbound")

        val expectedTableHeadings = listOf("System", "Description", "Technology")

        expectedTableHeadings.forEachIndexed { index, text ->
            assertThat(content.nth(1).locator("thead th").nth(index)).containsText(text)
        }
        expectedTableHeadings.forEachIndexed { index, text ->
            assertThat(content.nth(3).locator("thead th").nth(index)).containsText(text)
        }

        val outboundData = listOf(
            listOf(
                "E-mail System",
                "Sends e-mail using",
                ""
            ),
            listOf(
                "Mainframe Banking System",
                "Gets account information from, and makes payments using",
                ""
            )
        )

        outboundData.forEachIndexed { rowIndex, data ->
            data.forEachIndexed { cellIndex, text ->
                assertThat(content.nth(3).locator("tbody tr").nth(rowIndex).locator("td").nth(cellIndex)).containsText(text)
            }
        }
    }
}
