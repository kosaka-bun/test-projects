package de.honoka.test.microservice.product.config

import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.QueueBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitConfig {

    @Bean
    fun decreaseCountQueue(): Queue {
        return QueueBuilder.nonDurable("product.decreaseCountQueue").build()
    }
}