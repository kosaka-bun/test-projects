package de.honoka.test.kotlin.coroutine

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

object CoroutineTest4 {

    @JvmStatic
    fun main(args: Array<String>): Unit = runBlocking {
        launch {
            //runBlocking {
            launch {
                delay(100)
                printWithThread("print1")
            }
            launch {
                delay(200)
                printWithThread("print2")
            }
            printWithThread("print3")
        }
        printWithThread("print4")
    }
}