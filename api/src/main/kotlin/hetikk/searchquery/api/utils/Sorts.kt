package hetikk.searchquery.api.utils

import hetikk.searchquery.api.model.SearchQueryField
import hetikk.searchquery.api.model.sort.Sort
import hetikk.searchquery.api.model.sort.SortDirectionType

object Sorts {

    fun sort(field: SearchQueryField, direction: SortDirectionType): Sort {
        return Sort(field, direction)
    }

}
