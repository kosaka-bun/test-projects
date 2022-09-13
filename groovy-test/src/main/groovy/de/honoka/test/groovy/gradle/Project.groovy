package de.honoka.test.groovy.gradle

class Project {

    Plugins plugins = new Plugins()

    void plugins(Closure closure) {
        closure.delegate = plugins
        closure()
    }

    String group, version

    void group(String group) {
        this.group = group
    }

    void version(String version) {
        this.version = version
    }

    Repositories repositories = new Repositories()

    void repositories(Closure closure) {
        closure.delegate = repositories
        closure()
    }

    Dependencies dependencies = new Dependencies()

    void dependencies(Closure closure) {
        closure.delegate = dependencies
        closure()
    }

    def extension = new HashMap<String, String>()

    void ext(Closure closure) {
        closure.delegate = extension
        closure()
    }
}
