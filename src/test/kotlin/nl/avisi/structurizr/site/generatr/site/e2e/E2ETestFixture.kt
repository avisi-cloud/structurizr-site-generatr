package nl.avisi.structurizr.site.generatr.site.e2e

import assertk.assertThat
import assertk.assertions.isTrue
import com.microsoft.playwright.Browser
import com.microsoft.playwright.BrowserContext
import com.microsoft.playwright.Page
import com.microsoft.playwright.Playwright
import nl.avisi.structurizr.site.generatr.main
import org.junit.jupiter.api.*
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import java.time.Duration

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class E2ETestFixture {
    companion object {
        @JvmStatic
        protected val PORT = 9999

        @JvmStatic
        protected val SITE_URL = "http://127.0.0.1:$PORT"
    }

    private lateinit var playwright: Playwright
    private lateinit var browser: Browser
    private lateinit var context: BrowserContext
    protected lateinit var page: Page

    @BeforeAll
    @Order(1)
    fun serveExampleSite() {
        Thread(::serve).apply {
            isDaemon = true
            start()
        }

        var reachable = false
        var count = 0
        while (!reachable && count < 1000) {
            reachable = siteIsReachable()
            Thread.sleep(Duration.ofMillis(100))
            count++
        }
        assertThat(reachable, name = "site reachable").isTrue()
    }

    @BeforeAll
    @Order(2)
    fun launchBrowser() {
        playwright = Playwright.create()
        browser = playwright.chromium().launch()
    }

    @BeforeEach
    @Order(1)
    fun createContextAndPage() {
        context = browser.newContext()
        page = context.newPage()
    }

    @AfterEach
    fun closeContext() {
        context.close()
    }

    @AfterAll
    fun closeBrowser() {
        playwright.close()
    }

    private fun serve() {
        main(arrayOf("serve", "-w", "docs/example/workspace.dsl", "-p", PORT.toString()))
    }

    private fun siteIsReachable(): Boolean {
        Socket().use { socket ->
            try {
                socket.connect(InetSocketAddress("127.0.0.1", PORT))
            } catch (_: IOException) {
                return false
            }
            return true
        }
    }
}
