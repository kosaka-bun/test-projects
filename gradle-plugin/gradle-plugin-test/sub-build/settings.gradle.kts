@file:Suppress("UnstableApiUsage")

pluginManagement {
    val customRepositories: RepositoryHandler.() -> Unit = {
        mavenLocal()
        maven("https://maven.aliyun.com/repository/gradle-plugin")
        maven("https://maven.aliyun.com/repository/public")
        gradlePluginPortal()
        mavenCentral()
        maven("https://mirrors.honoka.de/maven-repo/release")
        maven("https://mirrors.honoka.de/maven-repo/development")
    }
    repositories(customRepositories)
    dependencyResolutionManagement {
        repositories(customRepositories)
    }
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "gradle-plugin-test-sub-build"

include("project3")
