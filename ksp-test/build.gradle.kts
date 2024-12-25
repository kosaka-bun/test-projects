import de.honoka.gradle.buildsrc.kotlin
import java.nio.charset.StandardCharsets

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    java
    alias(libs.plugins.dependency.management)
    alias(libs.plugins.kotlin)
    id("com.google.devtools.ksp") version "1.8.10-1.0.9"
}

group = "de.honoka.test"
version = libs.versions.root.get()

allprojects {
    apply(plugin = "java")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.jetbrains.kotlin.jvm")
    
    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = sourceCompatibility
    }
    
    dependencyManagement {
        imports {
            mavenBom(rootProject.libs.kotlin.bom.get().toString())
        }
    }
    
    dependencies {
        kotlin(project)
        rootProject.libs.versions.kotlin.coroutines
        implementation("cn.hutool:hutool-all:5.8.18")
        implementation("ch.qos.logback:logback-classic:1.5.6")
    }
    
    tasks {
        compileJava {
            options.encoding = StandardCharsets.UTF_8.name()
        }
        
        compileKotlin {
            kotlinOptions.jvmTarget = java.sourceCompatibility.toString()
        }
        
        test {
            useJUnitPlatform()
        }
    }
}

dependencies {
    implementation(project("ksp"))
    ksp(project("ksp"))
}