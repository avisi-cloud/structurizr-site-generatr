package nl.avisi.structurizr.site.generatr.site.e2e

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.microsoft.playwright.Locator
import com.microsoft.playwright.Page

class PageTestHelper {
    fun testMenu(page: Page, additionalActions: (Locator) -> Unit) {
        val menu = page.locator(".menu > *")

        val general = menu.nth(0)
        val softwareSystems = menu.nth(2)

        assertThat(general.innerText()).isEqualTo("GENERAL")
        assertThat(softwareSystems.innerText()).isEqualTo("SOFTWARE SYSTEMS")

        val generalMenu = menu.nth(1).locator("a")
        val softwareSystemsMenu = menu.nth(3).locator("a")

        assertThat(generalMenu.allInnerTexts()).isEqualTo(listOf(
            "Home",
            "Decisions",
            "Software Systems",
            "Embedding diagrams and images",
            "Extended Markdown features",
            "AsciiDoc features"
        ))
        assertThat(softwareSystemsMenu.allInnerTexts()).isEqualTo(listOf(
            "ATM",
            "E-mail System",
            "Internet Banking System",
            "Mainframe Banking System"
        ))

        additionalActions(menu)
    }

    fun testTitleAndSubtitle(page: Page, title: String, subtitle: String) {
        val titleLocator = page.locator(".container > .title")
        val subtitleLocator = page.locator(".container > .subtitle")

        assertThat(titleLocator.innerText()).isEqualTo(title)
        assertThat(subtitleLocator.innerText()).isEqualTo(subtitle)
    }

    fun testTabs(page: Page, additionalActions: (Locator) -> Unit) {
        val tabs = page.locator(".container > .tabs > ul > li")

        assertThat(tabs.allInnerTexts()).isEqualTo(listOf(
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
        ))

        additionalActions(tabs)
    }
}
