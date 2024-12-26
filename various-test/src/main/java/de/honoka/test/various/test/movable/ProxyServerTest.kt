package de.honoka.test.various.test.movable

import java.io.ByteArrayOutputStream
import java.net.ServerSocket
import java.net.Socket

@Suppress("MemberVisibilityCanBePrivate")
object ProxyServerTest {
    
    val serverSocket: ServerSocket = ServerSocket(10000)
    
    @JvmStatic
    fun main(args: Array<String>) {
        println(serverSocket)
        while(true) {
            val connection = serverSocket.accept()
            Thread {
                runCatching {
                    connection.use { handle(it) }
                }.exceptionOrNull()?.printStackTrace()
            }.start()
        }
    }
    
    fun handle(connection: Socket) {
        val os = ByteArrayOutputStream()
        runCatching {
            while(true) {
                val buffer = ByteArray(1024)
                val read = connection.getInputStream().read(buffer)
                if(read > 0) os.write(buffer, 0, read)
                if(read < 0) break
            }
        }
        println(String(os.toByteArray()))
    }
}
