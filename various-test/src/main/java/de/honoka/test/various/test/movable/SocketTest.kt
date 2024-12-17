@file:Suppress("MemberVisibilityCanBePrivate")

package de.honoka.test.various.test.movable

import cn.hutool.core.util.RandomUtil
import cn.hutool.http.Header
import cn.hutool.http.HttpUtil
import de.honoka.sdk.util.kotlin.net.http.browserHeaders
import de.honoka.sdk.util.kotlin.net.socket.SocketForwarder
import org.junit.Test
import java.net.Socket
import java.util.concurrent.TimeUnit

object SocketServerTest {
    
    val urlSet = setOf(
        "www.baidu.com:80",
        "www.sogou.com:80"
    )
    
    val socketForwarder = SocketForwarder(urlSet, timeoutOnEmptyForward = 2 * 1000)
    
    @JvmStatic
    fun main(args: Array<String>) {
        println(socketForwarder.port)
    }
}

object SocketClientTest {
    
    @JvmStatic
    fun main(args: Array<String>) {
        action()
    }
    
    fun action() {
        val socket = Socket("127.0.0.1", 10000)
        while(true) {
            socket.getOutputStream().write(ByteArray(RandomUtil.randomInt(70) + 1))
            TimeUnit.SECONDS.sleep(1)
        }
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
}