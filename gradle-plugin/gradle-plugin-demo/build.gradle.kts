import java.nio.charset.StandardCharsets

plugins {
    `java-gradle-plugin`
    `maven-publish`
    `kotlin-dsl`
}

group = "de.honoka.gradle.plugin"
version = libs.versions.root.get()

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = sourceCompatibility
    withSourcesJar()
}

gradlePlugin {
    plugins {
        create("demoPlugin") {
            id = "de.honoka.gradle.plugin.demo"
            implementationClass = "de.honoka.gradle.plugin.demo.DemoPlugin"
        }
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

publishing {
    repositories {
        mavenLocal()
    }
}
