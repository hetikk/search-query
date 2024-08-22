package hetikk.searchquery.api.utils

import hetikk.searchquery.api.exception.InvalidArgumentException
import hetikk.searchquery.api.model.SearchQueryField
import hetikk.searchquery.api.model.filter.SimpleFilter
import hetikk.searchquery.api.model.filter.FilterCondition
import hetikk.searchquery.api.model.filter.FilterOperationType
import hetikk.searchquery.api.model.filter.OperatorType

object Filters {

    fun condition(field: SearchQueryField, operation: FilterOperationType): FilterCondition {
        if (!operation.isUnary) {
            throw InvalidArgumentException("Unary operation is not supported: $operation")
        }
        return FilterCondition(field, operation, null)
    }

    fun condition(field: SearchQueryField, operation: FilterOperationType, value: Any?): FilterCondition {
        return FilterCondition(field, operation, value)
    }

    fun simpleFilter(operator: OperatorType, vararg conditions: FilterCondition): SimpleFilter {
        return SimpleFilter().apply {
            this.operator = operator
            conditions.forEach { condition ->
                this.conditions.add(condition)
            }
        }
    }

    fun simpleFilter(vararg conditions: FilterCondition): SimpleFilter {
        return simpleFilter(OperatorType.AND, *conditions)
    }

}
