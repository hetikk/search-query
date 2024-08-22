package hetikk.searchquery.api.model

import hetikk.searchquery.api.AbstractUnitTest
import hetikk.searchquery.api.exception.InvalidArgumentException
import hetikk.searchquery.api.model.filter.FilterOperationType
import hetikk.searchquery.api.utils.Filters
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class FilterConditionTest : AbstractUnitTest() {

    @Test
    fun checkTwoArgsConditionCreation() {
        var throwCount = 0
        FilterOperationType.entries.forEach {
            when (it.isUnary) {
                true -> assertDoesNotThrow { Filters.condition(testField, it) }
                false -> {
                    assertThrows<InvalidArgumentException> { Filters.condition(testField, it) }
                    throwCount++
                }
            }
        }

        val notUnaryOperationCount = FilterOperationType.entries.count { !it.isUnary }
        assertThat(throwCount).isEqualTo(notUnaryOperationCount)
    }

}
