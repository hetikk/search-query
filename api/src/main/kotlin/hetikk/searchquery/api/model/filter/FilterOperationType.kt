package hetikk.searchquery.api.model.filter

/**
 * The enum of all available filtering operations.
 *
 * @param isUnary indicates whether this operator is unary (i.e. does not require specifying a filtering value)
 */
enum class FilterOperationType(val isUnary: Boolean) {
    EQUAL(false),
    NOT_EQUAL(false),
    LIKE(false),
    NOT_LIKE(false),
    LIKE_CASE_INSENSITIVE(false),
    NOT_LIKE_CASE_INSENSITIVE(false),
    IS_TRUE(true),
    IS_FALSE(true),
    GREATER(false),
    GREATER_OR_EQUAL(false),
    LESS(false),
    LESS_OR_EQUAL(false),
    IN(false),
    NOT_IN(false),
    IS_NULL(true),
    IS_NOT_NULL(true),
    BETWEEN(false),
    NOT_BETWEEN(false),
}
