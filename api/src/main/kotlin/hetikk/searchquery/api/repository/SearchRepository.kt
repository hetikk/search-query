package hetikk.searchquery.api.repository

import hetikk.searchquery.api.model.SearchQuery
import hetikk.searchquery.api.model.SearchResponse

interface SearchRepository {

    fun <R> search(resultType: Class<R>, searchQueryLambda: () -> SearchQuery): SearchResponse<R>

}
