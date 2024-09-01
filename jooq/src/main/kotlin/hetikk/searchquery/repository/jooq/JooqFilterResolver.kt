package hetikk.searchquery.repository.jooq

import hetikk.searchquery.api.exception.InvalidArgumentException
import hetikk.searchquery.api.exception.UnknownFieldException
import hetikk.searchquery.api.model.SearchQuery
import hetikk.searchquery.api.model.SearchQueryField
import hetikk.searchquery.api.model.filter.FilterCondition
import hetikk.searchquery.api.model.filter.FilterOperationType.BETWEEN
import hetikk.searchquery.api.model.filter.FilterOperationType.EQUAL
import hetikk.searchquery.api.model.filter.FilterOperationType.GREATER
import hetikk.searchquery.api.model.filter.FilterOperationType.GREATER_OR_EQUAL
import hetikk.searchquery.api.model.filter.FilterOperationType.IN
import hetikk.searchquery.api.model.filter.FilterOperationType.IS_FALSE
import hetikk.searchquery.api.model.filter.FilterOperationType.IS_NOT_NULL
import hetikk.searchquery.api.model.filter.FilterOperationType.IS_NULL
import hetikk.searchquery.api.model.filter.FilterOperationType.IS_TRUE
import hetikk.searchquery.api.model.filter.FilterOperationType.LESS
import hetikk.searchquery.api.model.filter.FilterOperationType.LESS_OR_EQUAL
import hetikk.searchquery.api.model.filter.FilterOperationType.LIKE
import hetikk.searchquery.api.model.filter.FilterOperationType.LIKE_CASE_INSENSITIVE
import hetikk.searchquery.api.model.filter.FilterOperationType.NOT_BETWEEN
import hetikk.searchquery.api.model.filter.FilterOperationType.NOT_EQUAL
import hetikk.searchquery.api.model.filter.FilterOperationType.NOT_IN
import hetikk.searchquery.api.model.filter.OperatorType
import hetikk.searchquery.api.model.filter.SimpleFilter
import org.jooq.Condition
import org.jooq.Field
import org.jooq.Operator
import org.jooq.impl.DSL

object JooqFilterResolver {

    fun resolve(
        searchQuery: SearchQuery,
        fieldsMapper: Map<SearchQueryField, Field<*>>
    ): Condition {
        return when (searchQuery.filter) {
            is SimpleFilter -> conditionListFilter(
                filter = searchQuery.filter as SimpleFilter,
                fieldsMapper = fieldsMapper
            )

            else -> TODO()
        }
    }

    private fun conditionListFilter(
        filter: SimpleFilter,
        fieldsMapper: Map<SearchQueryField, Field<*>>
    ): Condition {
        val jooqOperator = when (filter.operator) {
            OperatorType.AND -> Operator.AND
            OperatorType.OR -> Operator.OR
        }
        val jooqConditions = filter.conditions.map { toJooqCondition(it, fieldsMapper) }
        return DSL.condition(jooqOperator, *jooqConditions.toTypedArray())
    }

    private fun toJooqCondition(
        filterCondition: FilterCondition,
        fieldsMapper: Map<SearchQueryField, Field<*>>
    ): Condition {
        val mappedField: Field<*> = fieldsMapper[filterCondition.field]
            ?: throw UnknownFieldException(filterCondition.field)
        val jooqField: Field<Any?> = mappedField as Field<Any?>

        val value: Any? = filterCondition.value

        return when (filterCondition.operation) {
            EQUAL -> jooqField.equal(value)

            NOT_EQUAL -> jooqField.notEqual(value)

            LIKE -> {
                val pattern = value as? String ?: throw InvalidArgumentException(
                    "The '${filterCondition.operation}' operation requires a value with the non-nullable STRING data type"
                )
                jooqField.like(pattern)
            }

            LIKE_CASE_INSENSITIVE -> {
                val pattern = value as? String ?: throw InvalidArgumentException(
                    "The '${filterCondition.operation}' operation requires a value with the non-nullable STRING data type"
                )
                jooqField.likeIgnoreCase(pattern)
            }

            IS_TRUE -> jooqField.isTrue

            IS_FALSE -> jooqField.isFalse

            GREATER -> jooqField.greaterThan(value)

            GREATER_OR_EQUAL -> jooqField.greaterOrEqual(value)

            LESS -> jooqField.lessThan(value)

            LESS_OR_EQUAL -> jooqField.lessOrEqual(value)

            IN -> jooqField.`in`(value)

            NOT_IN -> jooqField.notIn(value)

            IS_NULL -> jooqField.isNull

            IS_NOT_NULL -> jooqField.isNotNull

            BETWEEN -> {
                val list = value as? List<*> ?: throw InvalidArgumentException(
                    "The '${filterCondition.operation}' operation requires a value with the non-nullable LIST data type"
                )

                if (list.size != 2) {
                    throw InvalidArgumentException(
                        "The '${filterCondition.operation}' operation requires a value with a LIST " +
                                "of two elements is required"
                    )
                }

                jooqField.between(list[0], list[1])
            }

            NOT_BETWEEN -> {
                val list = value as? List<*> ?: throw InvalidArgumentException(
                    "The '${filterCondition.operation}' operation requires a value with the non-nullable LIST data type"
                )

                if (list.size != 2) {
                    throw InvalidArgumentException(
                        "The '${filterCondition.operation}' operation requires a value with a LIST " +
                                "of two elements is required"
                    )
                }

                jooqField.notBetween(list[0], list[1])
            }
        }
    }

}