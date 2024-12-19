@file:Suppress("MemberVisibilityCanBePrivate")

package de.honoka.test.various.test.movable

import cn.hutool.http.Header
import cn.hutool.http.HttpUtil
import de.honoka.sdk.util.kotlin.code.cast
import de.honoka.sdk.util.kotlin.code.exception
import de.honoka.sdk.util.kotlin.code.log
import de.honoka.sdk.util.kotlin.net.http.browserHeaders
import de.honoka.sdk.util.kotlin.net.socket.NioSocketClient
import org.junit.Test
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

object SocketServerTest {
    
    data class ClientConnection(
        
        var writable: Boolean = false,
        
        var target: SocketChannel? = null
    )
    
    val executor: ExecutorService = Executors.newFixedThreadPool(1)
    
    val serverSocketChannel: ServerSocketChannel = ServerSocketChannel.open().apply {
        configureBlocking(false)
        bind(InetSocketAddress(10000))
    }
    
    val selector: Selector = Selector.open().also {
        serverSocketChannel.register(it, SelectionKey.OP_ACCEPT)
    }
    
    val clients = ConcurrentHashMap<SocketChannel, ClientConnection>()
    
    @JvmStatic
    fun main(args: Array<String>) {
        executor.submit {
            log.info("Listening...")
            while(true) {
                runCatching {
                    action()
                }
            }
        }
    }
    
    fun action() {
        val count = selector.select()
        log.info("Selected $count.")
        val keys = selector.selectedKeys()
        keys.forEach {
            if(!it.isValid) return@forEach
            runCatching {
                when {
                    it.isAcceptable -> accept(it)
                    it.isReadable -> read(it)
                    it.isWritable -> writable(it)
                }
            }.exceptionOrNull()?.printStackTrace()
        }
        keys.clear()
    }
    
    fun accept(key: SelectionKey) {
        key.channel().cast<ServerSocketChannel>().accept().run {
            log.info("Client: $this")
            clients[this] = ClientConnection()
            configureBlocking(false)
            register(selector, SelectionKey.OP_READ or SelectionKey.OP_WRITE)
        }
    }
    
    fun read(key: SelectionKey) {
        key.channel().cast<SocketChannel>().run {
            val buffer = ByteBuffer.allocate(1024)
            try {
                val read = read(buffer)
                if(read < 0) exception()
                val data = buffer.array().sliceArray(0 until read)
                println(String(data))
            } catch(t: Throwable) {
                t.printStackTrace()
                log.info("Connection closed: $this")
                clients.remove(this)
                close()
            }
        }
    }
    
    fun writable(key: SelectionKey) {
        key.channel().cast<SocketChannel>().run {
            log.info("writable")
            write(ByteBuffer.wrap("hello2".toByteArray()))
            clients[this]?.writable = true
            register(selector, SelectionKey.OP_READ)
        }
    }
}

object SocketClientTest {
    
    val nioSocketClient = NioSocketClient()
    
    @JvmStatic
    fun main(args: Array<String>) {
        action()
    }
    
    fun action() {
        val connection = nioSocketClient.connect("127.0.0.1:10000")
        repeat(10) {
            nioSocketClient.refresh()
            TimeUnit.MILLISECONDS.sleep(500)
            println(connection)
            if(connection.readable) {
                println(String(connection.read()))
            }
            if(connection.writable) {
                connection.write("hello1".toByteArray())
            }
        }
        nioSocketClient.close()
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