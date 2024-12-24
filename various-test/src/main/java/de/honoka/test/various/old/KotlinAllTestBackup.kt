package de.honoka.test.various.old

import cn.hutool.core.date.DateField
import cn.hutool.core.date.DateTime
import cn.hutool.http.HttpUtil
import cn.hutool.jwt.JWT
import de.honoka.sdk.util.kotlin.basic.HasLogger
import de.honoka.sdk.util.kotlin.basic.log
import de.honoka.sdk.util.kotlin.net.http.browserApiHeaders
import de.honoka.sdk.util.kotlin.net.proxy.ProxyPool
import de.honoka.sdk.util.kotlin.text.forEachWrapper
import de.honoka.sdk.util.kotlin.text.simpleSingleLine
import de.honoka.sdk.util.kotlin.text.singleLine
import de.honoka.sdk.util.kotlin.text.toJsonWrapper
import org.junit.Test
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.nio.channels.ServerSocketChannel
import java.util.concurrent.TimeUnit

class KotlinAllTestBackup : HasLogger {
    
    @Test
    fun test9() {
        val server1 = ServerSocketChannel.open().apply {
            configureBlocking(false)
            bind(InetSocketAddress(20000))
        }
        val server2 = ServerSocketChannel.open().apply {
            configureBlocking(false)
            runCatching {
                bind(InetSocketAddress(20000))
            }.getOrElse {
                it.printStackTrace()
                bind(InetSocketAddress(20001))
                println("OK")
            }
        }
    }
    
    @Test
    fun test8() {
        log.info("test")
        log.debug("test2")
        log.error("test3")
    }
    
    @Test
    fun test7() {
        val server1 = ServerSocket(10000)
        //val server2 = ServerSocket(10000)
        //println("$server1 $server2")
        server1.close()
        println(server1.isClosed)
        server1.close()
    }
    
    @Test
    fun test6() {
        ProxyPool.instance.init()
        println(ProxyPool.instance.randomProxy)
    }
    
    @Test
    fun test5() {
        HttpUtil.createGet("http://httpbin.org/ip").run {
            browserApiHeaders()
            setHttpProxy("127.0.0.1", 10000)
            timeout(TimeUnit.SECONDS.toMillis(3).toInt())
            execute()
        }
    }
    
    @Test
    fun test4() {
        val regex = Regex("\\d{1,3}(\\.\\d{1,3}){3}:\\d{1,5}")
        listOf(
            "3.3.3.3:3030",
            "3.3:30",
            "123.123.123.123:8080",
            "123.123.123.123:808080",
            "123.123.123.1.23:8080",
            "123.123.123.123:"
        ).forEach {
            println(regex.matches(it))
        }
    }
    
    @Test
    fun test3() {
        val proxies = HttpUtil.get("https://www.docip.net/data/free.json").toJsonWrapper()
        proxies.getArray("data").forEachWrapper {
            val proxy = it.getStr("ip").split(":")
            HttpUtil.createGet("http://httpbin.org/ip").run {
                println(it.getStr("ip"))
                setHttpProxy(proxy[0], proxy[1].toInt())
                timeout(TimeUnit.SECONDS.toMillis(5).toInt())
                runCatching {
                    val res = execute()
                    println(res.status)
                    println(res.body())
                }
                println("\n")
            }
        }
    }
    
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
        val g = """
            abc
            def
            ghi
        """.simpleSingleLine(true)
        println(g)
        val h = """
            abc,,
            def,,
            ,ghi,
        """.singleLine(',', true)
        println(h)
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