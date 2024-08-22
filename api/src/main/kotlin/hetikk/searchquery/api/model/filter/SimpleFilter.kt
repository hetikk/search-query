package hetikk.searchquery.api.model.filter

/**
 * The implementation of a filter, which is a list of conditions combined by a specified logical operator.
 *
 * By default, the [OperatorType.AND] operator will be selected.
 *
 * @see OperatorType
 */
data class SimpleFilter(
    var operator: OperatorType = OperatorType.AND,
    var conditions: MutableList<FilterCondition> = mutableListOf()
) : Filter {

    override val type: FilterType = FilterType.SIMPLE

}

/**
 * A logical operator that specifies exactly how the conditions should be combined.
 */
enum class OperatorType {
    AND,
    OR;
}
