@file:Suppress("MemberVisibilityCanBePrivate")

package de.honoka.test.various.test.movable

import cn.hutool.http.Header
import cn.hutool.http.HttpUtil
import de.honoka.sdk.util.kotlin.net.http.browserHeaders
import de.honoka.sdk.util.kotlin.net.socket.NioSocketClient
import de.honoka.sdk.util.kotlin.net.socket.SocketForwarder
import org.junit.Test
import java.util.concurrent.TimeUnit

object SocketServerTest {
    
    val urlSet = setOf(
        "www.baidu.com:80",
        "www.sogou.com:80",
    )
    
    val socketForwarder = SocketForwarder(urlSet)
    
    @JvmStatic
    fun main(args: Array<String>) {
        println(socketForwarder.port)
    }
}

object SocketClientTest {
    
    val nioSocketClient = NioSocketClient()
    
    @JvmStatic
    fun main(args: Array<String>) {
        repeat(5) {
            action()
        }
    }
    
    fun action() {
        val connection = nioSocketClient.connect("127.0.0.1:10808")
        repeat(2) {
            nioSocketClient.refresh()
            TimeUnit.MILLISECONDS.sleep(500)
            println(connection)
            if(connection.readable) {
                println(String(connection.read()))
            }
            if(connection.writable) {
                connection.write("hello1".toByteArray())
            }
            println(connection.channel)
        }
        connection.close()
    }
}

class SocketHttpClientTest {
    
    @Test
    fun test1() {
        HttpUtil.createGet("http://localhost:10000").run {
            browserHeaders()
            header(Header.HOST, "www.baidu.com")
            println(execute().body())
        }
    }
    
    @Test
    fun test2() {
        fun req(port: Int) {
            HttpUtil.createGet("https://httpbin.org/ip").run {
                setHttpProxy("127.0.0.1", port)
                val res = execute()
                println(res.body())
                res.close()
            }
        }
        req(10808)
        req(10808)
        req(10808)
        req(10808)
        req(10808)
    }
}
