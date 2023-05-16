plugins {
    id("com.google.protobuf") version "0.9.3"
}

dependencies {

}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.22.2"
    }
    generatedFilesBaseDir = "$projectDir/build/generated/sources/protobuf/"
}