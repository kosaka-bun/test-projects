package de.honoka.test.various.test.movable

import org.junit.Test

class ReifiedTypeTest {

    private inline fun <reified T> fun1(): T? {
        val jClass = T::class.java
        val kClass = T::class
        println("$jClass\n$kClass")
        return null
    }

    @Test
    fun test1() {
        fun1<ArrayList<String>>()
    }
}