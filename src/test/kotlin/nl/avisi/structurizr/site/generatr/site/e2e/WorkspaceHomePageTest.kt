package nl.avisi.structurizr.site.generatr.site.e2e

import com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import kotlin.test.Test

class WorkspaceHomePageTest : E2ETestFixture() {
    private val pageTestHelper = PageTestHelper()

    @BeforeEach
    @Order(2)
    fun `navigate to page`() {
        page.navigate(SITE_URL)
    }

    @Test
    fun `title is correct`() {
        assertThat(page).hasTitle("Big Bank plc")
    }

    @Test
    fun `menu renders correctly`() {
        pageTestHelper.testMenu(page) { menu ->
            assertThat(menu.locator(".is-active")).hasText("Home")
        }
    }

    @Test
    fun `content renders correctly`() {
        val content = page.locator(".content > div > *")

        val title = content.nth(0)
        val paragraph1 = content.nth(1)
        val paragraph2 = content.nth(2)

        assertThat(title).containsText("Big Bank plc architecture")
        assertThat(paragraph1).containsText("This site contains the C4 architecture model for Big Bank plc.")
        assertThat(paragraph2).containsText("This page is the home page, because it is the first file in the workspace-docs directory, when the files are sorted alphabetically.")
    }
}
