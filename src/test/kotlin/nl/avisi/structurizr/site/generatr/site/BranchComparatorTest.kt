package nl.avisi.structurizr.site.generatr.site

import assertk.assertThat
import assertk.assertions.containsExactly
import nl.avisi.structurizr.site.generatr.branchComparator
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

class BranchComparatorTest {
    @TestFactory
    fun `default branch first then by string`() = listOf(
        listOf("a", "B", "main"),
        listOf("a", "main", "B"),
        listOf("B", "a", "main"),
        listOf("B", "main", "a"),
        listOf("main", "a", "B"),
        listOf("main", "B", "a"),
    ).map { branches ->
        DynamicTest.dynamicTest(branches.toString()) {
            assertThat(branches.sortedWith(branchComparator("main")))
                .containsExactly("main", "a", "B")
        }
    }
}
