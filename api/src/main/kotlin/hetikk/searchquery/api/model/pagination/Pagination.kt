package hetikk.searchquery.api.model.pagination

/**
 * Default page size
 */
const val DEFAULT_PAGE_SIZE = 20

/**
 * A basic interface that provides various pagination implementations.
 */
interface Pagination {
    val type: PaginationType
}

/**
 * Enum of available types of pagination
 */
enum class PaginationType {
    OFFSET,
}
