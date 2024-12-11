package de.honoka.test.various

import cn.hutool.core.date.DateField
import cn.hutool.core.date.DateTime
import cn.hutool.jwt.JWT
import de.honoka.sdk.util.kotlin.text.singleLine
import org.junit.Test

class KotlinAllTest {
    
    @Test
    fun test2() {
        val a = """
            abc
            def
        """.singleLine()
        println(a)
        val b = """
            abc
            def |
            ghi
        """.singleLine()
        println(b)
        val c = """
            abc
            | def |
            ghi
        """.singleLine()
        println(c)
        val d = """
            abc
            | |
            def
        """.singleLine()
        println(d)
        val e = """
            abc
            |
            def
        """.singleLine()
        println(e)
        val f = """
            abc
            | | |
            def
        """.singleLine()
        println(f)
    }

    @Test
    fun test1() {
        val key = "hello123".toByteArray()
        //生成
        val jwt1 = JWT.create().apply {
            setKey(key)
            addPayloads(mapOf("key" to "value"))
            val now = DateTime.now()
            setIssuedAt(now)
            setNotBefore(now)
            setExpiresAt(now.offsetNew(DateField.SECOND, 3))
        }
        val token = jwt1.sign()
        println(token)
        //验证
        val jwt2 = JWT(token).setKey(key)
        println(jwt2.payloads)
        println(jwt2.validate(0))
        Thread.sleep(1000)
        println(jwt2.validate(0))
        Thread.sleep(2500)
        println(jwt2.validate(0))
    }
}