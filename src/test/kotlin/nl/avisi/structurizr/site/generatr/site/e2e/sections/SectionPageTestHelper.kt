package nl.avisi.structurizr.site.generatr.site.e2e.sections

import com.microsoft.playwright.Page

class SectionPageTestHelper {
    fun testSectionContent(page: Page, additionalActions: (List<String>) -> Unit) {
        val content = page.locator(".content > div > *")

        val title = content.nth(0).innerText()
        val notes = content.nth(1).innerText()

        additionalActions(listOf(
            title,
            notes
        ))
    }
}
