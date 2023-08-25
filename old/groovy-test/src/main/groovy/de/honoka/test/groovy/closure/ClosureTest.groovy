package de.honoka.test.groovy.closure

import org.junit.jupiter.api.Test

class ClosureTest {

    @Test
    void test1() {
        def closure = {
            println this
            println delegate
        }
        new Class1().doClosure(closure)
        new Class2().doClosure(closure)
    }
}

class Class1 {

    void doClosure(Closure closure) {
        closure()
    }
}

class Class2 {

    void doClosure(Closure closure) {
        closure()
    }
}