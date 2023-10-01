package de.honoka.test.spring.transacational

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TransactionalTestApplication

fun main(args: Array<String>) {
    runApplication<TransactionalTestApplication>(*args)
}
