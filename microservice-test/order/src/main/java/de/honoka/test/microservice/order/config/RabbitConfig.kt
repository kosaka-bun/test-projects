package de.honoka.test.microservice.order.config

import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.QueueBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitConfig {

    @Bean
    fun orderQueue(): Queue {
        return QueueBuilder.nonDurable("order.queue").build()
    }
}