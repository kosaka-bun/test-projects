package de.honoka.test.groovy

import org.junit.jupiter.api.Test

class AllTest {

    @Test
    void test1() {
        println "Test1"
        JavaClass javaClass = new JavaClass()
        Closure<?> closure1 = {
            abcdef()
        }
        closure1.delegate = javaClass
        def map = new HashMap<String, String>()
        Closure<?> closure2 = {
            value1 = "value1"
        }
        closure2.delegate = map
        closure1()
        closure2()
        println map.value1
    }
}