import de.honoka.gradle.buildsrc.Versions
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.nio.charset.StandardCharsets

plugins {
    @Suppress("RemoveRedundantQualifierName")
    val versions = de.honoka.gradle.buildsrc.Versions
    //plugins
    java
    kotlin("jvm") version versions.kotlin
}

group = "de.honoka.test"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = sourceCompatibility
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}")
    implementation("org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinCoroutines}")
    implementation("de.honoka.sdk:honoka-kotlin-utils:2.0.0")
    implementation("cn.hutool:hutool-all:5.8.18")
    implementation("net.java.dev.jna:jna:5.3.1")
    implementation("com.sobte.cqp:jcq:1.2.7")
    implementation("com.baomidou:mybatis-plus:3.5.2")
    implementation("org.seleniumhq.selenium:selenium-java:4.27.0")
    implementation("org.seleniumhq.selenium:selenium-chrome-driver:4.27.0")
    compileOnly("org.projectlombok:lombok:1.18.26".also {
        annotationProcessor(it)
    })
    //Test
    implementation("org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}")
    implementation("junit:junit:4.13")
}

tasks {
    compileJava {
        options.encoding = StandardCharsets.UTF_8.name()
    }

    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = java.sourceCompatibility.toString()
    }

    test {
        useJUnitPlatform()
    }
}