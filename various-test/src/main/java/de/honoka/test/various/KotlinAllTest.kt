package de.honoka.test.various

import de.honoka.sdk.util.kotlin.basic.exception
import org.junit.Test

class KotlinAllTest {
    
    @Test
    fun test19() {
        try {
            exception("abc")
        } catch(t: Throwable) {
            println("catch")
            throw t
        } finally {
            println("finally")
        }
    }
}
