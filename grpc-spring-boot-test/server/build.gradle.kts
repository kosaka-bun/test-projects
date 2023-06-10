plugins {
    id("org.springframework.boot") version "2.7.12"
    kotlin("plugin.spring") version "1.7.22"
    kotlin("plugin.jpa") version "1.7.22"
}

dependencies {
    implementation(project(":common"))
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    //implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    //implementation("org.hibernate.orm:hibernate-community-dialects")
    //implementation("org.xerial:sqlite-jdbc")
    //implementation("mysql:mysql-connector-java:8.0.28")
    //implementation("com.baomidou:mybatis-plus-boot-starter:3.5.3.1")
    //https://yidongnan.github.io/grpc-spring-boot-starter/zh-CN/server/getting-started.html
    implementation("net.devh:grpc-server-spring-boot-starter:2.14.0.RELEASE")
    implementation("cn.hutool:hutool-all:5.8.18")
    implementation("commons-io:commons-io:2.11.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    //implementation("org.yaml:snakeyaml:1.33")
    //implementation("ch.qos.logback:logback-classic:1.4.5")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    //testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    //testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}