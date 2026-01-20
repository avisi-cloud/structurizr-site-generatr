package nl.avisi.structurizr.site.generatr.site.e2e.sections

import com.microsoft.playwright.Locator
import com.microsoft.playwright.Page
import com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat

class SectionsPageTestHelper {
    fun testSectionTabs(page: Page, additionalActions: (Locator) -> Unit) {
        val tabs = page.locator(".content .tabs:not(.is-size-7) a")

        additionalActions(tabs)
    }

    fun testComponentSectionTabs(page: Page, additionalActions: (Locator) -> Unit) {
        val tabs = page.locator(".content .tabs.is-size-7 a")

        additionalActions(tabs)
    }

    fun testSectionsTable(page: Page, additionalActions: (List<Locator>) -> Unit) {
        val tableHeadings = page.locator(".table thead th")
        val tableRows = page.locator(".table tbody tr")

        val locators = tableRows.all().map { row -> row.locator("td") }

        assertThat(tableHeadings).hasCount(2)

        assertThat(tableHeadings.nth(0)).containsText("#")
        assertThat(tableHeadings.nth(1)).containsText("Title")

        additionalActions(locators)
    }
}
