package de.honoka.test.various.test.movable.lambda

import org.junit.Test

class LambdaTest2 {

    @Test
    fun test1() {
        val jReceiver = LambdaReceiver<JavaBean>()
        jReceiver.receive { it.prop }
    }
}