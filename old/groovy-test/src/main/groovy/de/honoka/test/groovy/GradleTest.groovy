package de.honoka.test.groovy

import com.google.gson.GsonBuilder
import de.honoka.test.groovy.gradle.Project
import org.junit.jupiter.api.Test

class GradleTest {

    def project = new Project()

    @Override
    Object getProperty(String propertyName) {
        return project.extension.get(propertyName)
    }

    @Test
    void main() {
        def closure = {
            plugins {
                id 'groovy'
            }

            group 'de.honoka.test'
            version '1.0-SNAPSHOT'

            repositories {
                mavenCentral()
            }

            ext {
                groovyVersion = '4.0.2'
            }

            dependencies {
                implementation "org.apache.groovy:groovy:$groovyVersion"
                implementation 'com.alibaba:fastjson:2.0.12'
                implementation 'org.junit.jupiter:junit-jupiter-api:5.8.1'
                runtimeOnly 'org.junit.jupiter:junit-jupiter-engine:5.8.1'
            }
        }
        closure.delegate = project
        closure()
        def gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls().create()
        println gson.toJson(project)
    }
}
