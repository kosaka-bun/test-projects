package de.honoka.test.flyway

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FlywayTestApp

fun main(args: Array<String>) {
    runApplication<FlywayTestApp>(*args)
}