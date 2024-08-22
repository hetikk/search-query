package hetikk.searchquery.api.model

import hetikk.searchquery.api.model.filter.Filter
import hetikk.searchquery.api.model.pagination.Pagination
import hetikk.searchquery.api.model.sort.Sort

/**
 * A class that stores all the necessary information to perform a search.
 *
 * @property filter defines all the necessary filters
 * @property sort defines all the necessary sorts
 * @property pagination defines pagination
 * @property metaIsNeed determines whether metadata needs to be loaded
 *
 * @see [SearchResponse]
 * @see [Meta]
 */
data class SearchQuery(
    var filter: Filter? = null,
    var sort: List<Sort>? = null,
    var pagination: Pagination? = null,
    var metaIsNeed: Boolean = false,
)
