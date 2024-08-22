package hetikk.searchquery.api.model.sort

import hetikk.searchquery.api.model.SearchQueryField

/**
 * Specifies which field and how it should be sorted.
 *
 * @property field The name of the field to sort by.
 * @property direction The order in which to sort the field (ascending or descending).
 *
 */
data class Sort(
    var field: SearchQueryField,
    var direction: SortDirectionType
)

enum class SortDirectionType {
    ASC,
    DESC
}

/**
 * An extension that provides an alternative way to create a sort.
 *
 * Example usage:
 * ```
 * val sort = SOME_FIELD orderBy SortOrder.ASC
 * ```
 */
infix fun SearchQueryField.orderBy(direction: SortDirectionType): Sort {
    return Sort(this, direction)
}
