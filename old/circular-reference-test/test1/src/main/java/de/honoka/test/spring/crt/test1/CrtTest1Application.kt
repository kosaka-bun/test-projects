package de.honoka.test.spring.crt.test1

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CrtTest1Application

fun main(args: Array<String>) {
    runApplication<CrtTest1Application>(*args)
}