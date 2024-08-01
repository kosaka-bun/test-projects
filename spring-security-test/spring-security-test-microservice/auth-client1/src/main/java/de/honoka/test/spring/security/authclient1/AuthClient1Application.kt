package de.honoka.test.spring.security.authclient1

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AuthClient1Application

fun main(args: Array<String>) {
    runApplication<AuthClient1Application>(*args)
}