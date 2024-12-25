package de.honoka.test.various

import cn.hutool.core.date.DateTime
import cn.hutool.core.date.DateUnit
import cn.hutool.core.util.RandomUtil
import de.honoka.sdk.util.kotlin.basic.removeIf
import de.honoka.sdk.util.kotlin.io.ByteBufferIoStream
import de.honoka.sdk.util.kotlin.text.toJsonString
import org.junit.Test
import java.net.InetSocketAddress
import java.net.Socket
import java.nio.channels.SocketChannel
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class KotlinAllTest {
    
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
}