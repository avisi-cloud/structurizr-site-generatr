package nl.avisi.structurizr.site.generatr.site.e2e.sections

import assertk.assertThat
import assertk.assertions.isEqualTo
import nl.avisi.structurizr.site.generatr.site.e2e.E2ETestFixture
import nl.avisi.structurizr.site.generatr.site.e2e.PageTestHelper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import kotlin.test.Test

class SoftwareSystemContainerSectionPageTest : E2ETestFixture() {
    private val pageTestHelper = PageTestHelper()
    private val sectionPageTestHelper = SectionPageTestHelper()

    @BeforeEach
    @Order(2)
    fun `navigate to page`() {
        page.navigate(SITE_URL)

        page.locator(".menu a").and(page.getByText("Internet Banking System")).click()
        page.locator(".container > .tabs a").and(page.getByText("Documentation")).click()
        page.locator(".content > .tabs a").and(page.getByText("Database")).click()
        page.locator(".table a").and(page.getByText("Usage")).click()
    }

    @Test
    fun `title is correct`() {
        assertThat(page.title()).isEqualTo("Internet Banking System | Big Bank plc")
    }

    @Test
    fun `menu renders correctly`() {
        pageTestHelper.testMenu(page) { menu ->
            assertThat(menu.locator(".is-active").innerText()).isEqualTo("Internet Banking System")
        }
    }

    @Test
    fun `title and subtitle render correctly`() {
        pageTestHelper.testTitleAndSubtitle(
            page,
            "Internet Banking System",
            "Allows customers to view information about their bank accounts, and make payments."
        )
    }

    @Test
    fun `tabs render correctly`() {
        pageTestHelper.testTabs(page) { tabs ->
            assertThat(tabs.and(page.locator(".is-active")).innerText()).isEqualTo("Documentation")
        }
    }

    @Test
    fun `sections table renders correctly`() {
        sectionPageTestHelper.testSectionContent(page) { data ->
            assertThat(data.size).isEqualTo(2)

            assertThat(data[0]).isEqualTo("Usage")
            assertThat(data[1]).isEqualTo("This is how we use this thing.")
        }
    }
}
