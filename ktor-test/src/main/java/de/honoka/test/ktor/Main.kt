package de.honoka.test.ktor

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

object Main {

    @JvmStatic
    fun main(array: Array<String>) {
        embeddedServer(
            Netty,
            port = 8080,
            module = Application::module
        ).start(wait = true)
    }
}

fun Application.module() {
    configureRouting()
    configureHttpHandlers()
}
