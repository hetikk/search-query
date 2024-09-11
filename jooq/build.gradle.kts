//import org.jooq.meta.jaxb.Logging
//import org.testcontainers.containers.PostgreSQLContainer
//import org.testcontainers.containers.wait.strategy.Wait
//import org.testcontainers.ext.ScriptUtils
//import org.testcontainers.jdbc.JdbcDatabaseDelegate

//buildscript {
//    repositories {
//        mavenCentral()
//    }
//    dependencies {
//        classpath("org.testcontainers:testcontainers:${project.extra["testcontainersVersion"]}")
//        classpath("org.testcontainers:postgresql:${project.extra["testcontainersVersion"]}")
//        classpath("org.jooq:jooq-codegen:${project.extra["jooqVersion"]}")
//        classpath("org.postgresql:postgresql:${project.extra["postgresVersion"]}")
//    }
//}

plugins {
    id("nu.studer.jooq") version "9.0"
}

dependencies {
    // api module
    compileOnly(project(":api"))
    testImplementation(project(":api"))

    // jooq
    compileOnly("org.jooq:jooq:${project.extra["jooqVersion"]}")
    testImplementation("org.jooq:jooq-meta:${project.extra["jooqVersion"]}")
    testImplementation("org.jooq:jooq-codegen:${project.extra["jooqVersion"]}")
    jooqGenerator("org.postgresql:postgresql:${project.extra["postgresVersion"]}")

    // postgresql driver
    testImplementation("org.postgresql:postgresql:${project.extra["postgresVersion"]}")

    // testcontainers
    testImplementation("org.testcontainers:testcontainers:${project.extra["testcontainersVersion"]}")
    testImplementation("org.testcontainers:postgresql:${project.extra["testcontainersVersion"]}")
}

jooq {
    version.set("${project.extra["jooqVersion"]}")
    edition.set(nu.studer.gradle.jooq.JooqEdition.OSS)

    configurations {
        create("test") {
            generateSchemaSourceOnCompilation.set(true)
            jooqConfiguration.apply {
                logging = org.jooq.meta.jaxb.Logging.DEBUG
                jdbc.apply {
                    driver = "org.postgresql.Driver"
                    url = "jdbc:postgresql://localhost:5433/search_query_jooq"
                    user = "postgres"
                    password = "postgres"
                }
                generator.apply {
                    name = "org.jooq.codegen.DefaultGenerator"
                    database.apply {
                        name = "org.jooq.meta.postgres.PostgresDatabase"
                        inputSchema = "public"
                        isIncludeIndexes = true
                    }
                    target.apply {
                        packageName = "jooq"
                        directory = "$projectDir/src/test/generated"
                    }
                    strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
                }
            }
        }
    }
}

tasks.named("test") {
    dependsOn("generateTestJooq")
}

//tasks.named("generateJooq") {
//    doFirst {
//        println("Starting JOOQ code generation...")
////        PostgreContainer.start()
//    }
//    doLast {
//        println("Finished JOOQ code generation.")
////        PostgreContainer.stop()
//    }
//}

//object PostgreContainer {
//
//     const val PORT = 9876
//
//    private val container = PostgreSQLContainer("postgres:14.1-alpine").apply {
//        this.withDatabaseName("test")
//        this.withUsername("test")
//        this.withPassword("test")
////        this.withInitScript("init.sql")
//        this.withExposedPorts(PORT)
//        this.setPortBindings(listOf("$PORT:5432"))
//        this.waitingFor(Wait.forListeningPorts(PORT))
//
//        println()
//    }
//
////    fun jdbcUrl(): String = container.getJdbcUrl()
//
//    fun start() = container.start()
//
//    fun stop() = container.stop()
//
//}