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
    implementation("de.honoka.sdk:honoka-utils:1.0.8")
    implementation("junit:junit:4.13")
    implementation("net.java.dev.jna:jna:5.3.1")
    implementation("net.sourceforge.htmlunit:htmlunit:2.36.0")
    implementation("com.sobte.cqp:jcq:1.2.7")
    implementation("com.baomidou:mybatis-plus:3.5.2")
    compileOnly("org.projectlombok:lombok:1.18.26".also {
        annotationProcessor(it)
    })
    implementation("de.honoka.sdk:honoka-json-gson:1.0.0")
    //implementation("de.honoka.sdk:honoka-json-fastjson:1.0.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}")
    implementation("org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinCoroutines}")
    implementation("org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}")
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