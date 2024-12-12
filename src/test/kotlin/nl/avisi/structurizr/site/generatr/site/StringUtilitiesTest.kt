package nl.avisi.structurizr.site.generatr.site

import assertk.assertThat
import assertk.assertions.isEqualTo
import nl.avisi.structurizr.site.generatr.normalize
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

class StringUtilitiesTest {

    @TestFactory
    fun `normalize strips invalid chars`() = listOf(
        listOf("doc", "doc"),
        listOf("d c", "d-c"),
        listOf("doc:", "doc"),
        listOf(" doc ", "-doc-"),
        listOf("aux", "aux-"),
    ).map { (actual, expected) ->
        DynamicTest.dynamicTest("normalize replaces $actual with $expected") {
            assertThat(actual.normalize()).isEqualTo(expected)
        }
    }
}
