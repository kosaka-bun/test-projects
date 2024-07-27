import de.honoka.gradle.buildsrc.kotlin
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.nio.charset.StandardCharsets

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    java
    alias(libs.plugins.dependency.management)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.kotlin.jpa)
}

group = "de.honoka.test.spring.security"
version = libs.versions.root.get()

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = sourceCompatibility
}

dependencies {
    kotlin(project)
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation(libs.honoka.framework.utils)
    implementation("cn.hutool:hutool-all:5.8.18")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.hibernate.orm:hibernate-community-dialects")
    implementation("com.baomidou:mybatis-plus-spring-boot3-starter:3.5.5")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    runtimeOnly("org.xerial:sqlite-jdbc")
    //Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
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