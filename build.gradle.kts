plugins {
    kotlin("jvm") version "2.0.0"
}

repositories {
    mavenCentral()
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    ext {
        set("jooqVersion", "3.19.11")
        set("postgresVersion", "42.7.4")
        set("testcontainersVersion", "1.20.1")
    }

    group = "hetikk.search-query"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }

    dependencies {
        // junit
        testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.3")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.3")

        // assertj
        testImplementation("org.assertj:assertj-core:3.26.3")

        // mockk
        testImplementation("io.mockk:mockk:1.13.12")
    }

    tasks.test {
        useJUnitPlatform()
    }

    kotlin {
        jvmToolchain(17)
    }

}
