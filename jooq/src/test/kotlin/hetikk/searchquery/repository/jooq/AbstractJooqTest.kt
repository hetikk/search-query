package hetikk.searchquery.repository.jooq

import hetikk.searchquery.api.model.SearchQueryField
import hetikk.searchquery.repository.jooq.AbstractJooqTest.Companion.dsl
import hetikk.searchquery.repository.jooq.SearchQueryFields.DocumentFields
import java.sql.DriverManager
import java.time.Instant
import java.util.UUID
import jooq.Tables
import jooq.tables.Document
import org.jooq.DSLContext
import org.jooq.Field
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.wait.strategy.Wait

abstract class AbstractJooqTest {

    companion object {

        private val container = PostgreSQLContainer("postgres:15-alpine").apply {
            withInitScript("init.sql")
            withLogConsumer { frame -> println("postgres container log: " + frame.utf8String) }
            waitingFor(Wait.forListeningPort())
            start()
        }

        val dsl: DSLContext = DSL.using(
            DriverManager.getConnection(
                container.getJdbcUrl(),
                container.username,
                container.password
            ),
            SQLDialect.POSTGRES
        )

        val docRepo = DocumentRepositoryImpl()
    }

}

class SearchQueryFields {

    enum class DocumentFields : SearchQueryField {
        DOCUMENT_ID,
        TITLE,
        CREATED_AT,
        DOCUMENT_TYPE,
        IS_DELETED,
        DELETED_AT,
    }

    enum class DocumentContentFields : SearchQueryField {
        CONTENT_ID,
        DOCUMENT_ID,
        CONTENT,
    }

}

class DocumentRepositoryImpl : BaseJooqSearchRepository<Document>(dsl, Tables.DOCUMENT) {

    override val fieldsMapper: Map<SearchQueryField, Field<*>> = mapOf(
        DocumentFields.DOCUMENT_ID to table.DOCUMENT_ID,
        DocumentFields.TITLE to table.TITLE,
        DocumentFields.CREATED_AT to table.CREATED_AT,
        DocumentFields.DOCUMENT_TYPE to table.DOCUMENT_TYPE,
        DocumentFields.IS_DELETED to table.IS_DELETED,
        DocumentFields.DELETED_AT to table.DELETED_AT,
    )

}

data class Document(
    val documentId: UUID,
    val title: String,
    val createdAt: Instant,
    val documentType: DocumentType,
    val isDeleted: Boolean,
    val deletedAt: Instant?,
)

enum class DocumentType {
    WORD,
    EXCEL
}
