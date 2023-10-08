package de.honoka.test.httpproxy

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HttpProxyTestApplication

fun main(args: Array<String>) {
    runApplication<HttpProxyTestApplication>(*args)
}