package hetikk.searchquery.api.model

/**
 * A container class that stores the search result.
 *
 * @property data contains search result
 * @property meta contains metadata about search result
 */
data class SearchResponse<T>(
    val data: List<T>,
    val meta: Meta?,
)

/**
 * This class contains metadata about search result.
 */
open class Meta(
    open var totalCount: Long
)
