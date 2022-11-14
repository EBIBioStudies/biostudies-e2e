import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.20"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    //Cucumber and junit
    testImplementation("io.cucumber:cucumber-java:7.8.1")
    testImplementation("io.cucumber:cucumber-junit-platform-engine:7.8.1")
    testImplementation("org.junit.platform:junit-platform-suite:1.9.1")

    testImplementation("org.springframework:spring-web:5.3.23")// enables restTemplate
    testImplementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.0")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}