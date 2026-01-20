package nl.avisi.structurizr.site.generatr.site.e2e.sections

import com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat
import nl.avisi.structurizr.site.generatr.site.e2e.E2ETestFixture
import nl.avisi.structurizr.site.generatr.site.e2e.PageTestHelper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import kotlin.test.Test

class SoftwareSystemContainerSectionsPageTest : E2ETestFixture() {
    private val pageTestHelper = PageTestHelper()
    private val sectionsPageTestHelper = SectionsPageTestHelper()

    @BeforeEach
    @Order(2)
    fun `navigate to page`() {
        page.navigate(SITE_URL)

        page.locator(".menu a").and(page.getByText("Internet Banking System")).click()
        page.locator(".container > .tabs a").and(page.getByText("Documentation")).click()
        page.locator(".content > .tabs a").and(page.getByText("Database")).click()
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
            assertThat(tabs.and(page.locator(".is-active"))).containsText("Documentation")
        }
    }

    @Test
    fun `section tabs render correctly`() {
        sectionsPageTestHelper.testSectionTabs(page) { tabs ->
            assertThat(tabs).hasCount(3)

            assertThat(tabs.nth(0)).containsText("System")
            assertThat(tabs.nth(1)).containsText("API Application")
            assertThat(tabs.nth(2)).containsText("Database")

            assertThat(tabs.nth(2)).hasClass("is-active")
        }
    }

    @Test
    fun `component section tabs render correctly`() {
        sectionsPageTestHelper.testComponentSectionTabs(page) { tabs ->
            assertThat(tabs).hasCount(1)

            assertThat(tabs.nth(0)).containsText("Container")

            assertThat(tabs.nth(0)).hasClass("is-active")
        }
    }

    @Test
    fun `sections table renders correctly`() {
        sectionsPageTestHelper.testSectionsTable(page) { locators ->
            val expectedData = listOf(
                listOf("1", "Usage")
            )

            expectedData.forEachIndexed { rowIndex, row ->
                row.forEachIndexed { colIndex, text ->
                    assertThat(locators[rowIndex].nth(colIndex)).containsText(text)
                }
            }
        }
    }
}
