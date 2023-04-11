package de.honoka.test.spring.security

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MainClassConfig

object SpringSecurityTestApplication {

    @JvmStatic
    fun main(args: Array<String>) {
        runApplication<MainClassConfig>(*args)
    }
}