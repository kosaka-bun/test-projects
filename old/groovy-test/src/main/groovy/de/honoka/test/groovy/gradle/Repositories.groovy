package de.honoka.test.groovy.gradle

class Repositories {

    List<String> repositories = new ArrayList<>()

    void mavenCentral() {
        repositories.add("mavenCentral")
    }
}
