pluginManagement {
    repositories {
        maven("https://maven.aliyun.com/repository/central")
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "grpc-spring-boot-test"
include("common")
include("server")
include("client")