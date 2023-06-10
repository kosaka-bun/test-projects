dependencyManagement {
    imports {
        mavenBom("com.google.protobuf:protobuf-bom:3.23.2")
        mavenBom("com.google.guava:guava-bom:32.0.1-jre")
        mavenBom("io.grpc:grpc-bom:1.55.1")
        mavenBom("org.junit:junit-bom:5.9.3")
    }
}

dependencies {
    implementation("cn.hutool:hutool-all:5.8.18")
    implementation("commons-io:commons-io:2.11.0")
}

apply(from = "$rootDir/buildscripts/protobuf.gradle")