package de.honoka.test.spring.honoka.security

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HonokaStarterSecurityTestApp

fun main(args: Array<String>) {
    runApplication<HonokaStarterSecurityTestApp>(*args)
}