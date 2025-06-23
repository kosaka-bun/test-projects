import de.honoka.gradle.plugin.demo.ExtObj
import de.honoka.gradle.plugin.demo.extFun
import java.nio.charset.StandardCharsets

plugins {
    java
    alias(libs.plugins.dependency.management)
    alias(libs.plugins.kotlin)
    id("de.honoka.gradle.plugin.demo") version "1.0.0-dev"
}

group = "de.honoka.gradle.plugin"
version = libs.versions.root.get()

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = sourceCompatibility
}

dependencyManagement {
    imports {
        mavenBom(libs.kotlin.bom.get().toString())
    }
}

tasks {
    compileJava {
        options.run {
            encoding = StandardCharsets.UTF_8.name()
            val compilerArgs = compilerArgs as MutableCollection<String>
            compilerArgs += listOf("-parameters")
        }
    }

    compileKotlin {
        kotlinOptions {
            jvmTarget = java.sourceCompatibility.toString()
            freeCompilerArgs += listOf("-Xjsr305=strict", "-Xjvm-default=all")
        }
    }
}

demoPlugin {
    message = "test"
    otherMessage("123")
    extFun()
}

ExtObj.message = "test2"
