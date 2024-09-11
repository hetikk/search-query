package hetikk.searchquery.repository.jooq

import hetikk.searchquery.api.model.SearchQuery
import hetikk.searchquery.api.model.SearchResponse
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
import hetikk.searchquery.api.model.filter.FilterOperationType.NOT_LIKE
import hetikk.searchquery.api.model.filter.FilterOperationType.NOT_LIKE_CASE_INSENSITIVE
import hetikk.searchquery.api.model.filter.OperatorType.AND
import hetikk.searchquery.api.utils.Filters
import hetikk.searchquery.api.utils.Filters.condition
import hetikk.searchquery.repository.jooq.SearchQueryFields.DocumentFields.CREATED_AT
import hetikk.searchquery.repository.jooq.SearchQueryFields.DocumentFields.DELETED_AT
import hetikk.searchquery.repository.jooq.SearchQueryFields.DocumentFields.DOCUMENT_ID
import hetikk.searchquery.repository.jooq.SearchQueryFields.DocumentFields.IS_DELETED
import hetikk.searchquery.repository.jooq.SearchQueryFields.DocumentFields.TITLE
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import java.util.function.Consumer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class FilterOperationsTest : AbstractJooqTest() {

    @Test
    fun equal() {
        val uuid = "aeb1adf5-2644-482c-a5d3-29848205ad0c"

        val response = search(
            condition(DOCUMENT_ID, EQUAL, uuid)
        )

        assertThat(response.data)
            .isNotEmpty()
            .hasSize(1)

        val doc = response.data[0]
        assertThat(doc.documentId).isEqualTo(UUID.fromString(uuid))
        assertThat(doc.title).isEqualTo("Document1")
        assertThat(doc.createdAt).isEqualTo(instant("2024-09-10 23:00:00"))
        assertThat(doc.documentType).isEqualTo(DocumentType.WORD)
    }

    @Test
    fun notEqual() {
        val response = search(
            condition(TITLE, NOT_EQUAL, "Document1")
        )

        assertThat(response.data)
            .isNotEmpty()
            .hasSize(4)
            .extracting<String> { it.title }
            .satisfies(Consumer { titles ->
                assertThat(titles)
                    .doesNotContain("Document1")
                    .containsOnly(
                        "Document2",
                        "Document3",
                        "Document4",
                        "Document5"
                    )
            })
    }

    @Test
    fun like() {
        // like 'Doc%1'
        var response = search(
            condition(TITLE, LIKE, "Doc%1")
        )

        assertThat(response.data)
            .isNotEmpty()
            .hasSize(1)
            .extracting<String> { it.title }
            .containsOnly("Document1")

        // like 'doc%1'
        response = search(
            condition(TITLE, LIKE, "doc%1")
        )

        assertThat(response.data).isEmpty()
    }

    @Test
    fun notLike() {
        // not like 'Doc%1'
        var response = search(
            condition(TITLE, NOT_LIKE, "Doc%1")
        )

        assertThat(response.data)
            .isNotEmpty()
            .hasSize(4)
            .extracting<String> { it.title }
            .containsOnly(
                "Document2",
                "Document3",
                "Document4",
                "Document5",
            )

        // not like 'doc%1'
        response = search(
            condition(TITLE, NOT_LIKE, "doc%1")
        )

        assertThat(response.data)
            .isNotEmpty()
            .hasSize(5)
            .extracting<String> { it.title }
            .containsOnly(
                "Document1",
                "Document2",
                "Document3",
                "Document4",
                "Document5",
            )
    }

    @Test
    fun likeCaseInsensitive() {
        // ilike 'Doc%1'
        var response = search(
            condition(TITLE, LIKE_CASE_INSENSITIVE, "Doc%1")
        )

        assertThat(response.data)
            .isNotEmpty()
            .hasSize(1)
            .extracting<String> { it.title }
            .containsOnly("Document1")

        // ilike 'doc%1'
        response = search(
            condition(TITLE, LIKE_CASE_INSENSITIVE, "doc%1")
        )

        assertThat(response.data)
            .isNotEmpty()
            .hasSize(1)
            .extracting<String> { it.title }
            .containsOnly("Document1")
    }

    @Test
    fun notLikeCaseInsensitive() {
        // not ilike 'Doc%1'
        var response = search(
            condition(TITLE, NOT_LIKE_CASE_INSENSITIVE, "Doc%1")
        )

        assertThat(response.data)
            .isNotEmpty()
            .hasSize(4)
            .extracting<String> { it.title }
            .containsOnly(
                "Document2",
                "Document3",
                "Document4",
                "Document5",
            )

        // not ilike 'doc%1'
        response = search(
            condition(TITLE, NOT_LIKE_CASE_INSENSITIVE, "doc%1")
        )

        assertThat(response.data)
            .isNotEmpty()
            .hasSize(4)
            .extracting<String> { it.title }
            .containsOnly(
                "Document2",
                "Document3",
                "Document4",
                "Document5",
            )
    }

    @Test
    fun isTrue() {
        val response = search(
            condition(IS_DELETED, IS_TRUE)
        )

        assertThat(response.data)
            .isNotEmpty()
            .hasSize(3)
            .extracting<String> { it.title }
            .containsOnly(
                "Document1",
                "Document3",
                "Document5"
            )
    }

    @Test
    fun isFalse() {
        val response = search(
            condition(IS_DELETED, IS_FALSE)
        )

        assertThat(response.data)
            .isNotEmpty()
            .hasSize(2)
            .extracting<String> { it.title }
            .containsOnly(
                "Document2",
                "Document4",
            )
    }

    @Test
    fun greater() {
        val response = search(
            condition(CREATED_AT, GREATER, instant("2024-09-10 23:30:30"))
        )

        assertThat(response.data)
            .isNotEmpty()
            .hasSize(2)
            .extracting<Instant> { it.createdAt }
            .containsOnly(
                instant("2024-09-11 00:00:00"),
                instant("2024-09-11 01:00:00")
            )
    }

    @Test
    fun greaterOrEqual() {
        val response = search(
            condition(CREATED_AT, GREATER_OR_EQUAL, instant("2024-09-10 23:30:30"))
        )

        assertThat(response.data)
            .isNotEmpty()
            .hasSize(3)
            .extracting<Instant> { it.createdAt }
            .containsOnly(
                instant("2024-09-10 23:30:30"),
                instant("2024-09-11 00:00:00"),
                instant("2024-09-11 01:00:00")
            )
    }

    @Test
    fun less() {
        val response = search(
            condition(CREATED_AT, LESS, instant("2024-09-10 23:30:30"))
        )

        assertThat(response.data)
            .isNotEmpty()
            .hasSize(2)
            .extracting<Instant> { it.createdAt }
            .containsOnly(
                instant("2024-09-10 23:00:00"),
                instant("2024-09-10 23:30:00"),
            )
    }

    @Test
    fun lessOrEqual() {
        val response = search(
            condition(CREATED_AT, LESS_OR_EQUAL, instant("2024-09-10 23:30:30"))
        )

        assertThat(response.data)
            .isNotEmpty()
            .hasSize(3)
            .extracting<Instant> { it.createdAt }
            .containsOnly(
                instant("2024-09-10 23:00:00"),
                instant("2024-09-10 23:30:00"),
                instant("2024-09-10 23:30:30"),
            )
    }

    @Test
    fun `in`() {
        val response = search(
            condition(
                DOCUMENT_ID, IN, listOf(
                    "aeb1adf5-2644-482c-a5d3-29848205ad0c",
                    "16b82ef3-4050-4fe7-9553-194ee97e371b",
                    "62794b77-83d3-40d8-adad-2542cbd79670",
                )
            )
        )

        assertThat(response.data)
            .isNotEmpty()
            .hasSize(3)
            .extracting<UUID> { it.documentId }
            .containsOnly(
                UUID.fromString("aeb1adf5-2644-482c-a5d3-29848205ad0c"),
                UUID.fromString("16b82ef3-4050-4fe7-9553-194ee97e371b"),
                UUID.fromString("62794b77-83d3-40d8-adad-2542cbd79670"),
            )
    }

    @Test
    fun notIn() {
        val response = search(
            condition(
                DOCUMENT_ID, NOT_IN, listOf(
                    "aeb1adf5-2644-482c-a5d3-29848205ad0c",
                    "16b82ef3-4050-4fe7-9553-194ee97e371b",
                    "62794b77-83d3-40d8-adad-2542cbd79670",
                )
            )
        )

        assertThat(response.data)
            .isNotEmpty()
            .hasSize(2)
            .extracting<UUID> { it.documentId }
            .containsOnly(
                UUID.fromString("e30e35c6-1c50-461c-a2f7-14ce19bb3c6b"),
                UUID.fromString("aecce6c9-09e3-439b-8497-82d5f67f6241"),
            )
    }

    @Test
    fun isNull() {
        val response = search(
            condition(DELETED_AT, IS_NULL)
        )

        assertThat(response.data)
            .isNotEmpty()
            .hasSize(2)
            .anyMatch { it.title == "Document2" && !it.isDeleted && it.deletedAt == null }
            .anyMatch { it.title == "Document4" && !it.isDeleted && it.deletedAt == null }
    }

    @Test
    fun isNotNull() {
        val response = search(
            condition(DELETED_AT, IS_NOT_NULL)
        )

        assertThat(response.data)
            .isNotEmpty()
            .hasSize(3)
            .anyMatch { it.title == "Document1" && it.isDeleted && it.deletedAt == instant("2024-09-15 00:00:00") }
            .anyMatch { it.title == "Document3" && it.isDeleted && it.deletedAt == instant("2024-09-16 00:00:00") }
            .anyMatch { it.title == "Document5" && it.isDeleted && it.deletedAt == instant("2024-09-17 00:00:00") }
    }

    @Test
    fun between() {
        // date and time
        val response = search(
            condition(
                CREATED_AT, BETWEEN, listOf(
                    instant("2024-09-10 23:30:00"),
                    instant("2024-09-11 00:00:00")
                )
            )
        )

        assertThat(response.data)
            .isNotEmpty()
            .hasSize(3)
            .extracting<String> { it.title }
            .containsOnly(
                "Document2",
                "Document3",
                "Document4"
            )
    }

    @Test
    fun notBetween() {
        val response = search(
            condition(
                CREATED_AT, NOT_BETWEEN, listOf(
                    instant("2024-09-10 23:30:00"),
                    instant("2024-09-11 00:00:00")
                )
            )
        )

        assertThat(response.data)
            .isNotEmpty()
            .hasSize(2)
            .extracting<String> { it.title }
            .containsOnly(
                "Document1",
                "Document5"
            )
    }

    private fun search(condition: FilterCondition): SearchResponse<Document> {
        val searchQuery = SearchQuery().apply {
            filter = Filters.simpleFilter(AND, condition)
        }
        return docRepo.search(Document::class.java) { searchQuery }
    }

    private fun instant(dateTime: String, pattern: String = "yyyy-MM-dd HH:mm:ss"): Instant {
        val zonedDateTime = ZonedDateTime.now()
        val offset = zonedDateTime.offset

        val formatter = DateTimeFormatter.ofPattern(pattern)
        val localDateTime = LocalDateTime.parse(dateTime, formatter)
        val instant = localDateTime.toInstant(ZoneOffset.ofTotalSeconds(offset.totalSeconds))
        return instant
    }

}
