package nl.avisi.structurizr.site.generatr.site.e2e.decisions

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.microsoft.playwright.Locator
import com.microsoft.playwright.Page

class DecisionPageTestHelper {
    fun testDecisionContent(page: Page, additionalActions: (List<Locator>) -> Unit) {
        val content = page.locator(".content > div > *")

        val title = content.nth(0)
        val date = content.nth(1)
        val status = content.nth(3)
        val context = content.nth(5)
        val decision = content.nth(7)
        val consequences = content.nth(9)

        assertThat(content.nth(2).innerText()).isEqualTo("Status")
        assertThat(content.nth(4).innerText()).isEqualTo("Context")
        assertThat(content.nth(6).innerText()).isEqualTo("Decision")
        assertThat(content.nth(8).innerText()).isEqualTo("Consequences")

        additionalActions(listOf(
            title,
            date,
            status,
            context,
            decision,
            consequences
        ))
    }
}
