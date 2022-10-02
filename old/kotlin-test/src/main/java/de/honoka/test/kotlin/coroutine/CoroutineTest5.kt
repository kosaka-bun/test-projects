package de.honoka.test.kotlin.coroutine

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors

object CoroutineTest5 {

    @JvmStatic
    fun main(args: Array<String>) {
        val singleThreadContext = Executors.newSingleThreadExecutor()
                .asCoroutineDispatcher()
        GlobalScope.launch(singleThreadContext) {
            printWithThread("print1")
        }
        runBlocking(singleThreadContext) {
            printWithThread("print2")
        }
    }
}