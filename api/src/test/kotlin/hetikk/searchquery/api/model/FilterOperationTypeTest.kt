package hetikk.searchquery.api.model

import hetikk.searchquery.api.AbstractUnitTest
import hetikk.searchquery.api.model.filter.FilterOperationType
import hetikk.searchquery.api.model.filter.FilterOperationType.IS_FALSE
import hetikk.searchquery.api.model.filter.FilterOperationType.IS_NOT_NULL
import hetikk.searchquery.api.model.filter.FilterOperationType.IS_NULL
import hetikk.searchquery.api.model.filter.FilterOperationType.IS_TRUE
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class FilterOperationTypeTest : AbstractUnitTest() {

    @Test
    fun checkUnaryFilterOperations() {
        val unaryFilters = FilterOperationType.entries.filter { it.isUnary }

        assertThat(unaryFilters)
            .contains(IS_TRUE)
            .contains(IS_FALSE)
            .contains(IS_NULL)
            .contains(IS_NOT_NULL)
            .hasSize(4)
    }

}
