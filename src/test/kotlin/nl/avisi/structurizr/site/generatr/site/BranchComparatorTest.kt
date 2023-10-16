package nl.avisi.structurizr.site.generatr.site

import assertk.assertThat
import assertk.assertions.containsExactly
import nl.avisi.structurizr.site.generatr.branchComparator
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

class BranchComparatorTest {
    @TestFactory
    fun `default branch first then by string`() = listOf(
        listOf("a", "b", "main"),
        listOf("a", "main", "b"),
        listOf("b", "a", "main"),
        listOf("b", "main", "a"),
        listOf("main", "a", "b"),
        listOf("main", "b", "a")
    ).map { branches ->
        DynamicTest.dynamicTest(branches.toString()) {
            assertThat(branches.sortedWith(branchComparator("main")))
                .containsExactly("main", "a", "b")
        }
    }
}
