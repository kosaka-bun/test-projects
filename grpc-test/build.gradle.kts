import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        //maven("http://test.repo/dir") {
            //isAllowInsecureProtocol = true
        //}
        mavenCentral()
    }
}

plugins {
    id("java")
    kotlin("jvm") version "1.7.22" apply false
}

group = "de.honoka.test"
version = "1.0-SNAPSHOT"

subprojects {
    apply(plugin = "java")
    apply(plugin = "org.jetbrains.kotlin.jvm")

    group = rootProject.group
    version = rootProject.version
    java.sourceCompatibility = JavaVersion.VERSION_17

    repositories {
        //maven("http://test.repo/dir") {
            //isAllowInsecureProtocol = true
        //}
        mavenCentral()
    }

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        testImplementation("junit:junit:4.13.2")
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "17"
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}