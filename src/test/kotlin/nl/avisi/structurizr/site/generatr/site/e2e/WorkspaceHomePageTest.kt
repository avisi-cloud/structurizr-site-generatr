package nl.avisi.structurizr.site.generatr.site.e2e

import assertk.assertThat
import assertk.assertions.isEqualTo
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
        page.onDOMContentLoaded {
            assertThat(page.title()).isEqualTo("Big Bank plc")
        }
    }

    @Test
    fun `menu renders correctly`() {
        pageTestHelper.testMenu(page) { menu ->
            assertThat(menu.locator(".is-active").innerText()).isEqualTo("Home")
        }
    }

    @Test
    fun `content renders correctly`() {
        val content = page.locator(".content > div > *")

        val title = content.nth(0).innerText()
        val paragraph1 = content.nth(1).innerText()
        val paragraph2 = content.nth(2).innerText()

        assertThat(title).isEqualTo("Big Bank plc architecture")
        assertThat(paragraph1).isEqualTo("This site contains the C4 architecture model for Big Bank plc.")
        assertThat(paragraph2).isEqualTo("This page is the home page, because it is the first file in the workspace-docs directory, when the files are sorted alphabetically.")
    }
}
