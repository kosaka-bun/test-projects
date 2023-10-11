package de.honoka.test.grpc.server

import io.grpc.ServerBuilder

object Server {

    @JvmStatic
    fun main(args: Array<String>) {
        startServer()
    }

    private fun startServer() {
        val port = 8081
        val server = ServerBuilder.forPort(port).run {
            addService(TestEntityServiceImpl())
            build().start()
        }
        println("服务已启动，端口：$port")
        System.`in`.read()
        server.shutdown()
        server.awaitTermination()
    }
}
