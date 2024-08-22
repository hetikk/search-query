package hetikk.searchquery.api.validation

import hetikk.searchquery.api.AbstractUnitTest
import hetikk.searchquery.api.exception.InvalidArgumentException
import hetikk.searchquery.api.model.SearchQuery
import hetikk.searchquery.api.utils.Messages
import hetikk.searchquery.api.utils.Paginations
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class SearchQueryValidatorTest : AbstractUnitTest() {

    @Test
    fun checkPageValidation() {
        assertDoesNotThrow {
            SearchQueryValidator.validate(query(1, 1))
        }

        assertThrows<InvalidArgumentException>(Messages.ERROR_PAGE_LESS_ONE) {
            SearchQueryValidator.validate(query(0, 1))
        }

        assertThrows<InvalidArgumentException>(Messages.ERROR_PAGE_LESS_ONE) {
            SearchQueryValidator.validate(query(-1, 1))
        }
    }

    @Test
    fun checkCountValidation() {
        assertDoesNotThrow {
            SearchQueryValidator.validate(query(1, 1))
        }

        assertThrows<InvalidArgumentException>(Messages.ERROR_COUNT_LESS_ONE) {
            SearchQueryValidator.validate(query(1, 0))
        }

        assertThrows<InvalidArgumentException>(Messages.ERROR_COUNT_LESS_ONE) {
            SearchQueryValidator.validate(query(1, -1))
        }
    }

    private fun query(page: Int, count: Int): SearchQuery {
        return SearchQuery(
            pagination = Paginations.offsetLimit(page, count)
        )
    }

}
