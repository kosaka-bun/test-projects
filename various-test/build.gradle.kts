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
    implementation("de.honoka.sdk:honoka-kotlin-utils:1.0.2-dev")
    implementation("net.java.dev.jna:jna-platform:5.13.0")
    implementation("com.sobte.cqp:jcq:1.2.7")
    implementation("com.baomidou:mybatis-plus:3.5.2")
    implementation("org.seleniumhq.selenium:selenium-java:4.27.0")
    "org.projectlombok:lombok:1.18.26".let {
        compileOnly(it)
        annotationProcessor(it)
    }
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