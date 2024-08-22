package hetikk.searchquery.api.utils

import hetikk.searchquery.api.exception.InvalidArgumentException
import hetikk.searchquery.api.model.SearchQuery
import hetikk.searchquery.api.model.Meta
import hetikk.searchquery.api.model.pagination.OffsetLimitPagination
import hetikk.searchquery.api.model.pagination.OffsetLimitPaginationMeta
import hetikk.searchquery.api.model.pagination.PaginationType.OFFSET
import kotlin.math.ceil

object MetaUtils {

    fun forOffsetLimit(searchQuery: SearchQuery, totalCount: Long): Meta {
        if (searchQuery.pagination!!.type != OFFSET) {
            throw InvalidArgumentException("The 'searchQuery.pagination.type' must be equal to 'OFFSET'.")
        }

        val pagination = searchQuery.pagination as OffsetLimitPagination

        val currentPage: Int = pagination.page
        val pageSize: Int = pagination.count
        val totalPages: Int = ceil(totalCount.toDouble() / pageSize).toInt()

        return OffsetLimitPaginationMeta(
            totalCount = totalCount,
            currentPage = pagination.page,
            pageSize = pagination.count,
            totalPages = ceil(totalCount.toDouble() / pageSize).toInt(),
            hasNextPage = currentPage < totalPages,
            hasPreviousPage = 1 < currentPage,
        )
    }

}
