package nl.avisi.structurizr.site.generatr.site.e2e.decisions

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.microsoft.playwright.Locator
import com.microsoft.playwright.Page

class DecisionsPageTestHelper {
    fun testDecisionTabs(page: Page, additionalActions: (Locator) -> Unit) {
        val tabs = page.locator(".content .tabs:not(.is-size-7) a")

        additionalActions(tabs)
    }

    fun testComponentDecisionTabs(page: Page, additionalActions: (Locator) -> Unit) {
        val tabs = page.locator(".content .tabs.is-size-7 a")

        additionalActions(tabs)
    }

    fun testDecisionsTable(page: Page, additionalActions: (List<Locator>) -> Unit) {
        val tableHeadings = page.locator(".table thead th")
        val tableRows = page.locator(".table tbody tr")

        val locators = tableRows.all().map { row -> row.locator("td") }

        assertThat(tableHeadings.count()).isEqualTo(4)
        assertThat(tableHeadings.nth(0).innerText()).isEqualTo("ID")
        assertThat(tableHeadings.nth(1).innerText()).isEqualTo("Date")
        assertThat(tableHeadings.nth(2).innerText()).isEqualTo("Status")
        assertThat(tableHeadings.nth(3).innerText()).isEqualTo("Title")

        additionalActions(locators)
    }
}
