package hetikk.searchquery.api.pagination

import hetikk.searchquery.api.AbstractUnitTest
import hetikk.searchquery.api.model.SearchQuery
import hetikk.searchquery.api.model.pagination.DEFAULT_PAGE_SIZE
import hetikk.searchquery.api.model.pagination.OffsetLimitPagination
import hetikk.searchquery.api.model.pagination.OffsetLimitPaginationMeta
import hetikk.searchquery.api.utils.MetaUtils
import hetikk.searchquery.api.utils.Paginations
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class OffsetLimitPaginationTest : AbstractUnitTest() {

    @Test
    fun offsetLimit1() {
        val actual = MetaUtils.forOffsetLimit(
            searchQuery = searchQuery(1, 1),
            totalCount = 10
        )

        val expected = OffsetLimitPaginationMeta(
            totalCount = 10,
            totalPages = 10,
            currentPage = 1,
            pageSize = 1,
            hasNextPage = true,
            hasPreviousPage = false,
        )

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun offsetLimit2() {
        val actual = MetaUtils.forOffsetLimit(
            searchQuery = searchQuery(1, 10),
            totalCount = 10
        )

        val expected = OffsetLimitPaginationMeta(
            totalCount = 10,
            totalPages = 1,
            currentPage = 1,
            pageSize = 10,
            hasNextPage = false,
            hasPreviousPage = false,
        )

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun offsetLimit3() {
        val actual = MetaUtils.forOffsetLimit(
            searchQuery = searchQuery(2, 3),
            totalCount = 10
        )

        val expected = OffsetLimitPaginationMeta(
            totalCount = 10,
            totalPages = 4,
            currentPage = 2,
            pageSize = 3,
            hasNextPage = true,
            hasPreviousPage = true,
        )

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun offsetLimit4() {
        val actual = MetaUtils.forOffsetLimit(
            searchQuery = searchQuery(4, 3),
            totalCount = 10
        )

        val expected = OffsetLimitPaginationMeta(
            totalCount = 10,
            totalPages = 4,
            currentPage = 4,
            pageSize = 3,
            hasNextPage = false,
            hasPreviousPage = true,
        )

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun offsetLimit5() {
        val actual = MetaUtils.forOffsetLimit(
            searchQuery = searchQuery(5, 3),
            totalCount = 10
        )

        val expected = OffsetLimitPaginationMeta(
            totalCount = 10,
            totalPages = 4,
            currentPage = 5,
            pageSize = 3,
            hasNextPage = false,
            hasPreviousPage = true,
        )

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun offsetLimit6() {
        val actual = Paginations.offsetLimit(10, 10)

        val expected = OffsetLimitPagination(
            page = 10,
            count = 10
        )

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun offsetLimit7() {
        val actual = Paginations.offsetLimit(10)

        val expected = OffsetLimitPagination(
            page = 10,
            count = DEFAULT_PAGE_SIZE
        )

        assertThat(actual).isEqualTo(expected)
    }

    private fun searchQuery(page: Int, count: Int): SearchQuery {
        return SearchQuery(
            pagination = Paginations.offsetLimit(page, count)
        )
    }

}
