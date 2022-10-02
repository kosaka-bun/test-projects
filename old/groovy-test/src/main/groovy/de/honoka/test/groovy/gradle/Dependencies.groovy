package de.honoka.test.groovy.gradle

class Dependencies {

    List<String> dependencies = new ArrayList<>()

    void implementation(String str) {
        dependencies.add("implementation: $str".toString())
    }

    void runtimeOnly(String str) {
        dependencies.add("runtimeOnly: $str".toString())
    }
}
