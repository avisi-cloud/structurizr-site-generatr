package nl.avisi.structurizr.site.generatr.site.e2e.sections

import assertk.assertThat
import assertk.assertions.isEqualTo
import nl.avisi.structurizr.site.generatr.site.e2e.E2ETestFixture
import nl.avisi.structurizr.site.generatr.site.e2e.PageTestHelper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import kotlin.test.Test

class SoftwareSystemSectionsPageTest : E2ETestFixture() {
    private val pageTestHelper = PageTestHelper()
    private val sectionsPageTestHelper = SectionsPageTestHelper()

    @BeforeEach
    @Order(2)
    fun `navigate to page`() {
        page.navigate(SITE_URL)

        page.locator(".menu a").and(page.getByText("Internet Banking System")).click()
        page.locator(".container > .tabs a").and(page.getByText("Documentation")).click()
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
    fun `section tabs render correctly`() {
        sectionsPageTestHelper.testSectionTabs(page) { activeTab, tabs ->
            assertThat(tabs.size).isEqualTo(3)

            assertThat(tabs[0]).isEqualTo("System")
            assertThat(tabs[1]).isEqualTo("API Application")
            assertThat(tabs[2]).isEqualTo("Database")

            assertThat(activeTab).isEqualTo(tabs[0])
        }
    }

    @Test
    fun `component section tabs render correctly`() {
        sectionsPageTestHelper.testComponentSectionTabs(page) { _, data ->
            assertThat(data.size).isEqualTo(0)
        }
    }

    @Test
    fun `sections table renders correctly`() {
        sectionsPageTestHelper.testSectionsTable(page) { data ->
            assertThat(data.size).isEqualTo(2)

            assertThat(data[0]).isEqualTo(listOf("1", "History"))
            assertThat(data[1]).isEqualTo(listOf("2", "Usage"))
        }
    }
}
