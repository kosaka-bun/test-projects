package de.honoka.test.elasticsearch

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MainClassConfig

object ElasticSearchTestApplication {

    @JvmStatic
    fun main(args: Array<String>) {
        runApplication<MainClassConfig>(*args)
    }
}