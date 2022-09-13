package de.honoka.test.rabbitmq.consumer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Consumer1Application {

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<Consumer1Application>(*args)
        }
    }
}