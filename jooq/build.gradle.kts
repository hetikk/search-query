import nu.studer.gradle.jooq.JooqGenerate
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.utility.MountableFile

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.testcontainers:testcontainers:${project.extra["testcontainersVersion"]}")
        classpath("org.testcontainers:postgresql:${project.extra["testcontainersVersion"]}")
        classpath("org.postgresql:postgresql:${project.extra["postgresVersion"]}")
    }
}

plugins {
    id("nu.studer.jooq") version "9.0"
//    id("org.flywaydb.flyway") version "10.18.0"
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
                logging = org.jooq.meta.jaxb.Logging.WARN
                jdbc.apply {
                    driver = "org.postgresql.Driver"
                    // url will be configured in JooqGenerate task
                    user = "test"
                    password = "test"
                }
                generator.apply {
                    name = "org.jooq.codegen.DefaultGenerator"
                    database.apply {
                        name = "org.jooq.meta.postgres.PostgresDatabase"
                        inputSchema = "public"
                    }
                    generate.apply {
                        isDeprecated = false
                        isRecords = true
                        isPojos = false
                        isImmutablePojos = false
                        isFluentSetters = true
                        isDaos = false
                    }
                    target.apply {
                        packageName = "generated.jooq"
                        directory = "$projectDir/src/test/generated" // jooq module
                    }
                    strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
                }
            }
        }
    }
}

tasks.test {
    dependsOn("generateTestJooq")
}

tasks.register("testcontainersStart") {
    doLast {
        val dbInstance = PostgreSQLContainer<Nothing>("postgres:15-alpine").apply {
            withDatabaseName("test")
            withUsername("test")
            withPassword("test")
            withCopyToContainer(MountableFile.forHostPath("$projectDir/src/test/resources/db/init.sql"), "/docker-entrypoint-initdb.d/init.sql")
            waitingFor(Wait.forListeningPorts())
        }

        dbInstance.start()
        println("Starting container: ${dbInstance.containerId}")

        ext {
            set("pg_container_instance", dbInstance)
            set("pg_container_url", dbInstance.jdbcUrl)
        }
    }
}

tasks.register("testcontainersStop") {
    doLast {
        val dbInstance = project.ext["pg_container_instance"] as PostgreSQLContainer<*>
        println("Stopping container: ${dbInstance.containerId}")
        dbInstance.stop()
    }
}

tasks.withType<JooqGenerate>().configureEach {
    dependsOn("testcontainersStart")
    doFirst {
        jooq.configurations["test"].jooqConfiguration.jdbc.url = project.extra["pg_container_url"] as String
    }
    finalizedBy("testcontainersStop")
    allInputsDeclared.set(true)
}
