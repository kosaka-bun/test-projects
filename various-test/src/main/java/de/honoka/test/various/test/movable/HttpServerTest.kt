package de.honoka.test.various.test.movable

import com.sun.net.httpserver.HttpServer
import org.apache.http.HttpStatus
import java.net.InetSocketAddress

object HttpServerTest {

    @JvmStatic
    fun main(args: Array<String>) {
        HttpServer.create().apply {
            bind(InetSocketAddress(8080), 0)
            createContext("/test") {
                it.sendResponseHeaders(HttpStatus.SC_OK, 0)
                it.responseBody.writer().use { os ->
                    os.write("Hello JDK HTTP Server")
                }
            }
            start()
        }
        System.`in`.read()
    }
}