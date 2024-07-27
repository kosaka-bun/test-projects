package de.honoka.test.spring.security

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SecurityTestApp

fun main(args: Array<String>) {
    runApplication<SecurityTestApp>(*args)
}