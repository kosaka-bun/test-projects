package de.honoka.test.groovy.gradle

class Plugins {

    List<String> plugins = new ArrayList<>()

    void id(String id) {
        plugins.add(id)
    }
}
