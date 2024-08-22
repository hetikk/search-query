package hetikk.searchquery.api.model

import hetikk.searchquery.api.AbstractUnitTest
import hetikk.searchquery.api.model.sort.Sort
import hetikk.searchquery.api.model.sort.SortDirectionType
import hetikk.searchquery.api.model.sort.orderBy
import hetikk.searchquery.api.utils.Sorts
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class SortTest : AbstractUnitTest() {

    @Test
    fun checkSortInstances() {
        SortDirectionType.entries.forEach { direction ->
            val directSort = Sort(testField, direction)
            val infixSort = testField orderBy direction
            val staticSort = Sorts.sort(testField, direction)

            assertTrue(
                directSort == infixSort && infixSort == staticSort,
                "The Sort instances created by direct construction, infix function and static " +
                        "method should all be equal for '${direction}' order."
            )
        }
    }

}
