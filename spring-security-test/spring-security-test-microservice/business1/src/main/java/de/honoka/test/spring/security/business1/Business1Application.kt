package de.honoka.test.spring.security.business1

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@EnableFeignClients
@SpringBootApplication
class Business1Application

fun main(args: Array<String>) {
    runApplication<Business1Application>(*args)
}
