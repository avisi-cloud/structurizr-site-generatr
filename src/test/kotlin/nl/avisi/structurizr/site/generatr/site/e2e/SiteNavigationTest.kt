package nl.avisi.structurizr.site.generatr.site.e2e

import com.microsoft.playwright.Locator
import com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat
import com.microsoft.playwright.options.AriaRole
import org.junit.jupiter.api.Test

class SiteNavigationTest : E2ETestFixture() {
    @Test
    fun `redirect to default branch`() {
        page.navigate(SITE_URL)
        assertThat(page).hasURL("$SITE_URL/master/")
    }

    @Test
    fun `home page`() {
        page.navigate(SITE_URL)

        assertThat(page.getByRole(AriaRole.MAIN).getByRole(AriaRole.HEADING))
            .containsText("Big Bank plc architecture")
    }

    @Test
    fun `decisions page`() {
        page.navigate(SITE_URL)
        page.getByRole(AriaRole.NAVIGATION)
            .getByText("Decisions")
            .click()

        assertThat(page.getByRole(AriaRole.MAIN).getByRole(AriaRole.HEADING))
            .containsText("Decisions")
    }

    @Test
    fun `software systems page`() {
        page.navigate(SITE_URL)
        page.getByRole(AriaRole.NAVIGATION)
            .getByRole(AriaRole.LINK, Locator.GetByRoleOptions().setName("Software Systems"))
            .click()

        assertThat(page.getByRole(AriaRole.MAIN).getByRole(AriaRole.HEADING))
            .containsText("Software Systems")
    }
}
