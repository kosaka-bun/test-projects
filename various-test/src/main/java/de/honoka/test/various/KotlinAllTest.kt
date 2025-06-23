package de.honoka.test.various

import de.honoka.sdk.util.kotlin.basic.exception
import de.honoka.sdk.util.kotlin.various.RuntimeUtilsExt
import de.honoka.sdk.util.various.RuntimeUtils
import org.junit.Test
import java.nio.charset.StandardCharsets

class KotlinAllTest {

    @Test
    fun test20() {
        //println(RuntimeUtils.exec(RuntimeUtils.Command.win("java", "-version")))
        //println(RuntimeUtils.exec(RuntimeUtils.Command.win("taskkill", "/f", "/im", "chrome.exe")))
        println(RuntimeUtilsExt.exec {
            win("java", "-version")
        })
        println(RuntimeUtilsExt.exec {
            //charset(StandardCharsets.UTF_8)
            win("taskkill", "/f", "/im", "chrome.exe")
        })
    }
    
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
