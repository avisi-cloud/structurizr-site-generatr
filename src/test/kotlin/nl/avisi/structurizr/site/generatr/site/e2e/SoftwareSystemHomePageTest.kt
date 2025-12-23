package nl.avisi.structurizr.site.generatr.site.e2e

import assertk.assertThat
import assertk.assertions.isEqualTo
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
            assertThat(tabs.and(page.locator(".is-active")).innerText()).isEqualTo("Info")
        }
    }

    @Test
    fun `content renders correctly`() {
        val content = page.locator(".content > div > *")

        assertThat(content.nth(0).innerText()).isEqualTo("Description")
        assertThat(content.nth(2).innerText()).isEqualTo("Vision")
        assertThat(content.nth(4).innerText()).isEqualTo("References")

        assertThat(content.nth(1).innerText()).isEqualTo("This is our fancy Internet Banking System.")
        assertThat(content.nth(3).innerText()).isEqualTo("One system to rule them all!")
        assertThat(content.nth(5).innerText()).isEqualTo("Source Code on GitHub: [https://github.com/avisi-cloud/structurizr-site-generatr]")
    }

    @Test
    fun `properties render correctly`() {
        val properties = page.locator(".content > :not(div)")

        assertThat(properties.nth(0).innerText()).isEqualTo("Properties")

        val table = properties.and(page.locator(".table"))
        val tableHeadings = table.locator("thead th")

        assertThat(tableHeadings.nth(0).innerText()).isEqualTo("Name")
        assertThat(tableHeadings.nth(1).innerText()).isEqualTo("Value")

        val propertyValues = table.locator("tbody tr").all().map { row -> row.locator("td").all().map { it.innerText() } }

        assertThat(propertyValues[0]).isEqualTo(listOf("Development Team", "Dev/Internet Services"))
        assertThat(propertyValues[1]).isEqualTo(listOf("Owner", "Customer Services"))
        assertThat(propertyValues[2]).isEqualTo(listOf("Url", "https://en.wikipedia.org/wiki/Online_banking"))
    }
}
