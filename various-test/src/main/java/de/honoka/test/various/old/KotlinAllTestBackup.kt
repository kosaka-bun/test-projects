package de.honoka.test.various.old

import cn.hutool.core.date.DateField
import cn.hutool.core.date.DateTime
import cn.hutool.core.date.DateUnit
import cn.hutool.core.date.DateUtil
import cn.hutool.core.util.RandomUtil
import cn.hutool.http.HttpUtil
import cn.hutool.jwt.JWT
import de.honoka.sdk.util.concurrent.ThreadPoolUtils
import de.honoka.sdk.util.kotlin.basic.log
import de.honoka.sdk.util.kotlin.basic.removeIf
import de.honoka.sdk.util.kotlin.concurrent.ScheduledTask
import de.honoka.sdk.util.kotlin.io.ByteBufferIoStream
import de.honoka.sdk.util.kotlin.net.http.browserApiHeaders
import de.honoka.sdk.util.kotlin.text.*
import org.junit.Test
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class KotlinAllTestBackup {

    @Test
    fun test18() {
        val t = RuntimeException(null as String?)
        throw t
    }

    @Test
    fun test17() {
        val task = ScheduledTask("1s") {
            for(i in 1..100) {
                println(DateUtil.now())
                runCatching {
                    Thread.sleep(500)
                }.getOrElse {
                    it.printStackTrace()
                }
                println(Thread.currentThread().isInterrupted)
            }
        }
        task.startup()
        TimeUnit.SECONDS.sleep(5)
        task.close()
        println("closed")
    }

    @Test
    fun test16() {
        val runnable = Runnable {
            log.info("start - ${Thread.currentThread().name}")
            TimeUnit.SECONDS.sleep(5)
            log.info("end - ${Thread.currentThread().name}")
        }
        val executor = ThreadPoolUtils.newEagerThreadPool(
            1, 3, 60, TimeUnit.SECONDS
        )
        repeat(5) {
            Thread.sleep(500)
            executor.submit(runnable)
        }
        TimeUnit.SECONDS.sleep(60)
    }

    @Test
    fun test15() {
        //val socket = Socket("127.0.0.1", 10808)
        val channel = SocketChannel.open()
        channel.run {
            connect(InetSocketAddress("127.0.0.1", 10808))
            configureBlocking(false)
        }
        println()
    }

    @Test
    fun test14() {
        val map = ConcurrentHashMap<Int, String>().apply {
            put(1, "a")
            put(2, "b")
            put(3, "c")
        }
        println(map)
        map.removeIf { (k) -> k == 2 }
        println(map)
    }

    @Test
    fun test13() {
        val map = ConcurrentHashMap<Int, Int>().also {
            it[1] = 1
            it[2] = 3
            it[3] = 5
        }
        Thread {
            map.forEach {
                println(it)
                //map.remove(2)
                TimeUnit.SECONDS.sleep(1)
            }
            //map.forEach { k, v ->
            //    println("$k=$v")
            //    TimeUnit.SECONDS.sleep(1)
            //}
        }.start()
        Thread.sleep(500)
        map.remove(2)
        println(map)
        TimeUnit.SECONDS.sleep(10)
    }

    @Test
    fun test12() {
        val executor = Executors.newFixedThreadPool(2)
        val bufferIoStream = ByteBufferIoStream()
        executor.submit {
            while(true) {
                val bytes = RandomUtil.randomBytes(RandomUtil.randomInt(5, 8))
                //val b = (127..255).toList().toTypedArray()
                //val c = b.map { it.toByte() }.toTypedArray()
                //val d = c.map { it.toUByte().toInt() }.toTypedArray()
                println("write: ${bytes.toJsonString()}")
                bufferIoStream.asOut().write(bytes)
                Thread.sleep(1000)
            }
        }
        executor.submit {
            while(true) {
                Thread.sleep(500)
                System.err.println("read:  ${bufferIoStream.read(3).toJsonString()}")
            }
        }
        //Thread.sleep(500)
        //exitProcess(0)
        TimeUnit.SECONDS.sleep(60)
    }

    @Test
    fun test11() {
        val stream = ByteBufferIoStream().apply {
            write(byteArrayOf(1, 2, 3))
        }
        val buffer = ByteArray(10)
        println(stream.available())
        println(buffer.toJsonString())
        runCatching {
            stream.asIn().read(buffer, 5, 5)
        }.exceptionOrNull()?.printStackTrace()
        println(stream.available())
        println(buffer.toJsonString())
    }

    @Test
    fun test10() {
        val date1 = DateTime.now()
        TimeUnit.MILLISECONDS.sleep(3800)
        val date2 = DateTime.now()
        println(date2.between(date1, DateUnit.SECOND))
    }
    
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