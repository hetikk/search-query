package hetikk.searchquery.api.model.pagination

import hetikk.searchquery.api.model.Meta

/**
 * Pagination implementation based on offset.
 */
data class OffsetLimitPagination(
    var page: Int,
    var count: Int,
) : Pagination {
    override val type: PaginationType = PaginationType.OFFSET
}

/**
 * This class contains metadata about search result with offset-limit pagination.
 */
data class OffsetLimitPaginationMeta (
    override var totalCount: Long,
    val totalPages: Int,
    val currentPage: Int,
    val pageSize: Int,
    val hasNextPage: Boolean,
    val hasPreviousPage: Boolean,
) : Meta(totalCount)