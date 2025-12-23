package nl.avisi.structurizr.site.generatr.site.e2e.decisions

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.microsoft.playwright.Locator
import com.microsoft.playwright.Page

class DecisionsPageTestHelper {
    fun testDecisionTabs(page: Page, additionalActions: (String, List<String>) -> Unit) {
        val tabs = page.locator(".content .tabs:not(.is-size-7) a")
        val activeTab = tabs.and(page.locator(".is-active"))

        additionalActions(activeTab.innerText(), tabs.all().map { tab -> tab.innerText() })
    }

    fun testComponentDecisionTabs(page: Page, additionalActions: (Locator, List<String>) -> Unit) {
        val tabs = page.locator(".content .tabs.is-size-7 a")

        val data = tabs.all().map { tab -> tab.innerText() }

        additionalActions(tabs, data)
    }

    fun testDecisionsTable(page: Page, additionalActions: (List<List<String>>) -> Unit) {
        val tableHeadings = page.locator(".table thead th")
        val tableRows = page.locator(".table tbody tr")

        val data = tableRows.all().map { row -> row.locator("td").all().map { cell -> cell.innerText() } }

        assertThat(tableHeadings.count()).isEqualTo(4)
        assertThat(tableHeadings.nth(0).innerText()).isEqualTo("ID")
        assertThat(tableHeadings.nth(1).innerText()).isEqualTo("Date")
        assertThat(tableHeadings.nth(2).innerText()).isEqualTo("Status")
        assertThat(tableHeadings.nth(3).innerText()).isEqualTo("Title")

        additionalActions(data)
    }
}
