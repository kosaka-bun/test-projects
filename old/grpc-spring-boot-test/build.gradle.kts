import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java")
    id("com.google.protobuf") version "0.9.3" apply false
    id("io.spring.dependency-management") version "1.1.0" apply false
    kotlin("jvm") version "1.7.22" apply false
}

allprojects {
    apply(plugin = "java")

    group = "de.honoka.test"
    version = "1.0.0-SNAPSHOT"
    java.sourceCompatibility = JavaVersion.VERSION_17

    repositories {
        maven("https://maven.aliyun.com/repository/central")
        mavenCentral()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "17"
        }
    }

    tasks.getByName<Test>("test") {
        useJUnitPlatform()
    }
}

subprojects {
    apply(plugin = "com.google.protobuf")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.jetbrains.kotlin.jvm")
}