package nl.avisi.structurizr.site.generatr.site.e2e

import com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import kotlin.test.Test

class SoftwareSystemHomePageTest : E2ETestFixture() {
    private val pageTestHelper = PageTestHelper()

    @BeforeEach
    @Order(2)
    fun `navigate to page`() {
        page.navigate(SITE_URL)

        page.locator(".menu a").and(page.getByText("Internet Banking System")).click()
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
            assertThat(tabs.and(page.locator(".is-active"))).containsText("Info")
        }
    }

    @Test
    fun `content renders correctly`() {
        val content = page.locator(".content > div > *")

        assertThat(content.nth(0)).containsText("Description")
        assertThat(content.nth(2)).containsText("Vision")
        assertThat(content.nth(4)).containsText("References")

        assertThat(content.nth(1)).containsText("This is our fancy Internet Banking System.")
        assertThat(content.nth(3)).containsText("One system to rule them all!")
        assertThat(content.nth(5)).containsText("Source Code on GitHub: [https://github.com/avisi-cloud/structurizr-site-generatr]")
    }

    @Test
    fun `properties render correctly`() {
        val properties = page.locator(".content > :not(div)")

        assertThat(properties.nth(0)).containsText("Properties")

        val table = properties.and(page.locator(".table"))
        val tableHeadings = table.locator("thead th")

        assertThat(tableHeadings.nth(0)).containsText("Name")
        assertThat(tableHeadings.nth(1)).containsText("Value")

        val expectedPropertyValues = listOf(
            listOf(
                "Development Team",
                "Dev/Internet Services"
            ),
            listOf(
                "Owner",
                "Customer Services"
            ),
            listOf(
                "Url",
                "https://en.wikipedia.org/wiki/Online_banking"
            ),
        )

        expectedPropertyValues.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { colIndex, text ->
                assertThat(table.locator("tbody tr").nth(rowIndex).locator("td").nth(colIndex)).containsText(text)
            }
        }
    }
}
