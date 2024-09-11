package hetikk.searchquery.repository.jooq

import hetikk.searchquery.api.exception.UnknownFieldException
import hetikk.searchquery.api.model.Meta
import hetikk.searchquery.api.model.SearchQuery
import hetikk.searchquery.api.model.SearchQueryField
import hetikk.searchquery.api.model.SearchResponse
import hetikk.searchquery.api.model.pagination.OffsetLimitPagination
import hetikk.searchquery.api.model.sort.SortDirectionType
import hetikk.searchquery.api.utils.MetaUtils
import hetikk.searchquery.api.validation.SearchQueryValidator
import org.jooq.DSLContext
import org.jooq.Field
import org.jooq.Record
import org.jooq.Result
import org.jooq.SelectWhereStep
import org.jooq.impl.TableImpl

abstract class BaseJooqSearchRepository<T : TableImpl<*>>(
    protected val dsl: DSLContext,
    protected val table: T
) : JooqSearchRepository {

    abstract val fieldsMapper: Map<SearchQueryField, Field<*>>

    override fun <R> search(resultType: Class<R>, searchQueryLambda: () -> SearchQuery): SearchResponse<R> {
        val searchQuery = searchQueryLambda()
        SearchQueryValidator.validate(searchQuery)
        val data = fetch(searchQuery).into(resultType)
        val meta = if (searchQuery.metaIsNeed) {
            meta(searchQuery)
        } else {
            null
        }
        return SearchResponse(
            data = data,
            meta = meta
        )
    }

    override fun <R> search(searchQuery: SearchQuery, mapper: (Record) -> R): SearchResponse<R> {
        SearchQueryValidator.validate(searchQuery)
        val data = fetch(searchQuery).map(mapper)
        val meta = if (searchQuery.metaIsNeed) {
            meta(searchQuery)
        } else {
            null
        }
        return SearchResponse(
            data = data,
            meta = meta
        )
    }

    protected open fun queryRoot(): SelectWhereStep<Record> {
        val query = dsl.select(table.asterisk())
        query.from(table)
        return query
    }

    private fun fetch(request: SearchQuery): Result<Record> {
        val query = queryRoot()
        filter(request, query)
        sort(request, query)
        pagination(request, query)
        return query.fetch()
    }

    private fun filter(request: SearchQuery, query: SelectWhereStep<Record>) {
        query.where(JooqFilterResolver.resolve(request, fieldsMapper))
    }

    private fun sort(request: SearchQuery, query: SelectWhereStep<Record>) {
        request.sort?.forEach {
            val jooqField = fieldsMapper[it.field] ?: throw UnknownFieldException(it.field, table.name)

            val sortField = when (it.direction) {
                SortDirectionType.ASC -> jooqField.asc()
                SortDirectionType.DESC -> jooqField.desc()
            }

            query.orderBy(sortField)
        }
    }

    private fun pagination(request: SearchQuery, query: SelectWhereStep<Record>) {
        request.pagination?.let {
            when (it) {
                is OffsetLimitPagination -> {
                    query.offset((it.page - 1) * it.count)
                    query.limit(it.count)
                }

                else -> UnsupportedOperationException("The pagination type '${it.type}' is not implemented.")
            }
        }
    }

    private fun meta(request: SearchQuery): Meta {
        val query = queryRoot()
        val totalCount: Int = dsl.fetchCount(query)
        return MetaUtils.forOffsetLimit(request, totalCount.toLong())
    }

}