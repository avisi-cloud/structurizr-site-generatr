package nl.avisi.structurizr.site.generatr.site.e2e

import com.microsoft.playwright.Locator
import com.microsoft.playwright.Page
import com.microsoft.playwright.assertions.LocatorAssertions
import com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat

class PageTestHelper {
    fun testMenu(page: Page, additionalActions: (Locator) -> Unit) {
        val menu = page.locator(".menu > *")

        val general = menu.nth(0)
        val softwareSystems = menu.nth(2)

        assertThat(general).containsText("GENERAL", LocatorAssertions.ContainsTextOptions().setUseInnerText(true))
        assertThat(softwareSystems).containsText("SOFTWARE SYSTEMS", LocatorAssertions.ContainsTextOptions().setUseInnerText(true))

        val generalMenu = menu.nth(1).locator("a")
        val softwareSystemsMenu = menu.nth(3).locator("a")

        val generalMenuTexts = listOf(
            "Home",
            "Decisions",
            "Software Systems",
            "Embedding diagrams and images",
            "Extended Markdown features",
            "AsciiDoc features"
        )

        generalMenuTexts.forEachIndexed { index, text ->
            assertThat(generalMenu.nth(index)).containsText(text)
        }

        val softwareSystemsMenuTexts = listOf(
            "ATM",
            "E-mail System",
            "Internet Banking System",
            "Mainframe Banking System"
        )

        softwareSystemsMenuTexts.forEachIndexed { index, text ->
            assertThat(softwareSystemsMenu.nth(index)).containsText(text)
        }

        additionalActions(menu)
    }

    fun testTitleAndSubtitle(page: Page, title: String, subtitle: String) {
        val titleLocator = page.locator(".container > .title")
        val subtitleLocator = page.locator(".container > .subtitle")

        assertThat(titleLocator).containsText(title)
        assertThat(subtitleLocator).containsText(subtitle)
    }

    fun testTabs(page: Page, additionalActions: (Locator) -> Unit) {
        val tabs = page.locator(".container > .tabs > ul > li")

        val tabTexts = listOf(
            "Info",
            "Context views",
            "Container views",
            "Component views",
            "Code views",
            "Dynamic views",
            "Deployment views",
            "Dependencies",
            "Decisions",
            "Documentation"
        )

        tabTexts.forEachIndexed { index, text ->
            assertThat(tabs.nth(index)).containsText(text)
        }

        additionalActions(tabs)
    }
}
