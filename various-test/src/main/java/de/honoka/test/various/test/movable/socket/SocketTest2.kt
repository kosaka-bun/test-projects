@file:Suppress("MemberVisibilityCanBePrivate")

package de.honoka.test.various.test.movable.socket

import cn.hutool.http.HttpUtil
import de.honoka.sdk.util.kotlin.basic.log
import de.honoka.sdk.util.kotlin.basic.off
import de.honoka.sdk.util.kotlin.net.socket.NioSocketClient
import de.honoka.sdk.util.kotlin.net.socket.SocketConnection
import de.honoka.sdk.util.kotlin.net.socket.SocketForwarder
import de.honoka.sdk.util.kotlin.net.socket.selector.StatusSelector
import org.junit.Test
import java.util.concurrent.TimeUnit

object SocketServerTest2 {

    init {
        StatusSelector::class.log.off()
        SocketConnection::class.log.off()
        NioSocketClient::class.log.off()
    }

    @JvmStatic
    fun main(args: Array<String>) {
        //println(socketForwarder.port)
        TimeUnit.MINUTES.sleep(60)
    }
}

object SocketClientTest2 {

    val nioSocketClient = NioSocketClient()

    @JvmStatic
    fun main(args: Array<String>) {
        repeat(2) {
            action()
        }
    }

    fun action() {
        val connection = nioSocketClient.connect("127.0.0.1:10000")
        repeat(2) {
            nioSocketClient.refresh()
            TimeUnit.MILLISECONDS.sleep(500)
            println(connection)
            if(connection.readable) {
                println(String(connection.read()))
            }
            if(connection.writable) {
                connection.tryWrite("hello1".toByteArray())
            }
            println(connection.channel)
        }
        connection.close()
    }
}

class SocketHttpClientTest2 {

    val proxy by lazy { SocketForwarder(setOf("127.0.0.1:10808")) }

    @Test
    fun test1() {
        HttpUtil.createGet("http://httpbin.org/ip").run {
            //setHttpProxy("127.0.0.1", 10000)
            //setHttpProxy("127.0.0.1", 10808)
            setHttpProxy("127.0.0.1", proxy.port)
            val res = execute()
            println(res.body())
            res.close()
        }
    }

    @Test
    fun test2() {
        repeat(3) {
            HttpUtil.createGet("https://httpbin.org/ip").run {
                //setHttpProxy("127.0.0.1", 10000)
                //setHttpProxy("127.0.0.1", 10808)
                setHttpProxy("127.0.0.1", proxy.port)
                val res = execute()
                println(res.body())
                res.close()
            }
        }
    }
}
