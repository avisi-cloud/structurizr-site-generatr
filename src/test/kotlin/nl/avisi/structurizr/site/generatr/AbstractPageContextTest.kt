package nl.avisi.structurizr.site.generatr

import kotlin.io.path.Path
import kotlin.io.path.relativeTo
import kotlin.test.Test

class AbstractPageContextTest {
    @Test
    fun `relative path spike`() {
        val a = Path("a/b/c/d")
        val b = Path("a/b/E/F")

        println(b.relativeTo(a))
    }
}
