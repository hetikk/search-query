package hetikk.searchquery.api.validation

import hetikk.searchquery.api.exception.InvalidArgumentException
import hetikk.searchquery.api.model.SearchQuery
import hetikk.searchquery.api.model.pagination.OffsetLimitPagination
import hetikk.searchquery.api.model.pagination.Pagination
import hetikk.searchquery.api.utils.Messages

object SearchQueryValidator {

    fun validate(searchQuery: SearchQuery) {
        validatePagination(searchQuery.pagination)
    }

    private fun validatePagination(pagination: Pagination?) {
        if (pagination == null) {
            return
        }

        when (pagination) {
            is OffsetLimitPagination -> {
                if (pagination.page <= 0) {
                    throw InvalidArgumentException(Messages.ERROR_PAGE_LESS_ONE)
                }
                if (pagination.count <= 0) {
                    throw InvalidArgumentException(Messages.ERROR_COUNT_LESS_ONE)
                }
            }
        }
    }

}
