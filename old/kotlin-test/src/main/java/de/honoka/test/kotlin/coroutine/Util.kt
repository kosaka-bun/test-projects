package de.honoka.test.kotlin.coroutine

fun printWithThread(obj: Any?) {
    println("[${Thread.currentThread().name}] [${System.currentTimeMillis()}] ${
        obj?.toString()}")
}