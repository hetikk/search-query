ext {
    set("jooqVersion", "3.15.5")
}

dependencies {
    // api module
    compileOnly(project(":api"))

    // jooq
    compileOnly("org.jooq:jooq:${project.extra["jooqVersion"]}")
}
