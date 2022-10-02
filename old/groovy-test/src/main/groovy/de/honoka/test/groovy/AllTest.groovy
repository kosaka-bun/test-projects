package de.honoka.test.groovy

import org.junit.jupiter.api.Test

class AllTest {

    @Test
    void test1() {
        println "Test1"
        JavaClass javaClass = new JavaClass()
        Closure<?> clousure1 = {
            abcdef()
        }
        clousure1.delegate = javaClass
        def map = new HashMap<String, String>()
        Closure<?> clousure2 = {
            value1 = "value1"
        }
        clousure2.delegate = map
        clousure1()
        clousure2()
        println map.value1
    }
}