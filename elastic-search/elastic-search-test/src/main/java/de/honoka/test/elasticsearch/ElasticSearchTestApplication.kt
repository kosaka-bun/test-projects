package de.honoka.test.elasticsearch

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EnableJpaRepositories("de.honoka.test.elasticsearch.dao.jpa")
@SpringBootApplication
class MainClassConfig

object ElasticSearchTestApplication {

    @JvmStatic
    fun main(args: Array<String>) {
        runApplication<MainClassConfig>(*args)
    }
}