package hetikk.searchquery.api.model.filter

/**
 * A basic interface that provides various filter implementations.
 */
interface Filter {
    val type: FilterType
}

enum class FilterType {
    SIMPLE,
}
