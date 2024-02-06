package de.honoka.test.various.test.movable

object ClassExtendsTest {

    abstract class Main(private val str1: String) {

        init {
            println(str1)
            doTest()
        }

        private fun doTest() {
            test1()
        }

        abstract fun test1()
    }

    class Sub(private val str2: String) : Main("test") {

        override fun test1() {
            println(str2)
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        Sub("test2").test1()
    }
}