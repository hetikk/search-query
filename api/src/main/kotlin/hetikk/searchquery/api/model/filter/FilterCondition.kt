package hetikk.searchquery.api.model.filter

import hetikk.searchquery.api.model.SearchQueryField

/**
 * The basic element of any filter.
 * It specifies which field and how it should be filtered.
 */
data class FilterCondition(
    var field: SearchQueryField,
    var operation: FilterOperationType,
    var value: Any?,
)
