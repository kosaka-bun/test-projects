package de.honoka.test.httpproxy

import java.net.ServerSocket

object TestSocketServer {

    @JvmStatic
    fun main(args: Array<String>) {
        val server = ServerSocket(8080).apply {
            println("Waiting...")
        }
        var socket = server.accept()
        var reader = socket.getInputStream().bufferedReader()
        fun accpetNew() = runCatching {
            socket.close()
            reader.close()
            socket = server.accept()
            reader = socket.getInputStream().bufferedReader()
        }
        while(true) try {
            val line = reader.readLine()
            println("Received: $line")
            if(line?.isBlank() != false) accpetNew()
        } catch(t: Throwable) {
            t.printStackTrace()
            accpetNew()
        }
    }
}