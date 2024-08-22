plugins {
    kotlin("jvm")
}

group = "hetikk.search-query"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // junit
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.3")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.3")

    // assertj
    testImplementation("org.assertj:assertj-core:3.26.3")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}
