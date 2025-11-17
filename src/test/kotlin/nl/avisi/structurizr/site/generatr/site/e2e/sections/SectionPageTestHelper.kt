package nl.avisi.structurizr.site.generatr.site.e2e.sections

import com.microsoft.playwright.Locator
import com.microsoft.playwright.Page

class SectionPageTestHelper {
    fun testSectionContent(page: Page, additionalActions: (List<Locator>) -> Unit) {
        val content = page.locator(".content > div > *")

        val title = content.nth(0)
        val notes = content.nth(1)

        additionalActions(listOf(
            title,
            notes
        ))
    }
}
