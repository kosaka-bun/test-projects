plugins {
    id("org.springframework.boot") version "2.7.12"
    kotlin("plugin.spring") version "1.7.22"
    //kotlin("plugin.jpa") version "1.7.22"
}

dependencyManagement {
    imports {
        mavenBom("com.google.protobuf:protobuf-bom:3.23.2")
        mavenBom("com.google.guava:guava-bom:32.0.1-jre")
        mavenBom("io.grpc:grpc-bom:1.55.1")
        mavenBom("org.junit:junit-bom:5.9.3")
    }
}

dependencies {
    implementation(project(":common"))
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    //implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    //https://yidongnan.github.io/grpc-spring-boot-starter/zh-CN/client/getting-started.html
    implementation("net.devh:grpc-client-spring-boot-starter:2.14.0.RELEASE")
    implementation("com.google.protobuf:protobuf-java")
    implementation("com.google.protobuf:protobuf-java-util")
    implementation("cn.hutool:hutool-all:5.8.18")
    implementation("commons-io:commons-io:2.11.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}