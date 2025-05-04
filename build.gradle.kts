plugins {
    kotlin("jvm") version "1.9.0"
    java
    kotlin("plugin.serialization") version "1.9.0"
    id("io.gitlab.arturbosch.detekt") version ("1.23.8")
    id("com.ncorti.ktfmt.gradle") version "0.22.0"
    application
    idea
}

group = "com.gulaii"

version = "0.1.0"

dependencies {
    // Ktor server
    implementation("io.ktor:ktor-server-core:2.0.1")
    implementation("io.ktor:ktor-server-netty:2.0.1")
    implementation("io.ktor:ktor-server-content-negotiation:2.0.1")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.0.1")

    // Lettuce для KeyDB/Redis
    implementation("io.lettuce:lettuce-core:6.1.8.RELEASE")

    // JSON сериализация
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")

    // Логирование
    implementation("ch.qos.logback:logback-classic:1.2.11")

    // PostgreSQL с PostGIS
    implementation("org.postgresql:postgresql:42.3.6")
    implementation("org.jetbrains.exposed:exposed-core:0.38.2")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.38.2")

    implementation("com.github.Gylaii:keydb-client-lib:v0.1.1")
}

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

detekt {
    config.setFrom(files("${projectDir}/config/detekt.yml"))
    buildUponDefaultConfig = true
}

ktfmt {
    kotlinLangStyle()
    maxWidth.set(80)
    removeUnusedImports.set(false)
    manageTrailingCommas.set(true)
}

sourceSets {
    main {
        kotlin { srcDir("src") }
        resources { srcDirs("config") }
    }
}

application { mainClass.set("ApplicationKt") }
