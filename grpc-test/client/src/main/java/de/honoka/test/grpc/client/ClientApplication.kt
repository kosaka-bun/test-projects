package de.honoka.test.grpc.client

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MainConfig

object ClientApplication {

    @JvmStatic
    fun main(args: Array<String>) {
        runApplication<MainConfig>(*args)
    }
}