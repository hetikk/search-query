package hetikk.searchquery.repository.jooq

import hetikk.searchquery.api.model.SearchQuery
import hetikk.searchquery.api.model.SearchResponse
import hetikk.searchquery.api.repository.SearchRepository
import org.jooq.Record

interface JooqSearchRepository : SearchRepository {

    fun <R> search(searchQuery: SearchQuery, mapper: (Record) -> R): SearchResponse<R>

}
