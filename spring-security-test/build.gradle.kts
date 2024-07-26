import de.honoka.gradle.buildsrc.kotlin
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.nio.charset.StandardCharsets

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    java
    alias(libs.plugins.dependency.management)
    alias(libs.plugins.spring.boot) apply false
    alias(libs.plugins.kotlin) apply false
    alias(libs.plugins.kotlin.spring) apply false
    alias(libs.plugins.kotlin.jpa) apply false
}

version = libs.versions.root.get()

allprojects {
    group = "de.honoka.test.spring.security"

    apply(plugin = "io.spring.dependency-management")
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "org.jetbrains.kotlin.plugin.jpa")

    val libs = rootProject.libs

    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = sourceCompatibility
    }

    dependencyManagement {
        imports {
            mavenBom(libs.spring.cloud.dependencies.get().toString())
            mavenBom(libs.spring.cloud.alibaba.dependencies.get().toString())
        }
    }

    dependencies {
        kotlin(project)
        implementation("org.springframework.boot:spring-boot-starter")
        implementation("org.springframework.cloud:spring-cloud-starter-bootstrap")
        implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-discovery")
        implementation("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-config")
        implementation("org.springframework.cloud:spring-cloud-starter-loadbalancer")
        implementation(libs.honoka.framework.utils)
        implementation("cn.hutool:hutool-all:5.8.18")
        //Test
        testImplementation("org.springframework.boot:spring-boot-starter-test")
    }

    dependencies {
        if(project.name.contains("gateway")) return@dependencies
        implementation("org.springframework.boot:spring-boot-starter-web")
        runtimeOnly("com.h2database:h2")
        implementation("org.springframework.boot:spring-boot-starter-data-jpa")
        implementation("com.baomidou:mybatis-plus-boot-starter:3.5.5")
        implementation("org.springframework.boot:spring-boot-starter-data-redis")
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
}