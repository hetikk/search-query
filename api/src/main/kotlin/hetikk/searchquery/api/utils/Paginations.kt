package hetikk.searchquery.api.utils

import hetikk.searchquery.api.model.pagination.DEFAULT_PAGE_SIZE
import hetikk.searchquery.api.model.pagination.OffsetLimitPagination

object Paginations {

    fun offsetLimit(page: Int, limit: Int): OffsetLimitPagination {
        return OffsetLimitPagination(page, limit)
    }

    fun offsetLimit(page: Int): OffsetLimitPagination {
        return offsetLimit(page, DEFAULT_PAGE_SIZE)
    }

}
