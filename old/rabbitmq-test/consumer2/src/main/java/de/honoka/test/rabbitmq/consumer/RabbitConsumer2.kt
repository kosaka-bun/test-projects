package de.honoka.test.rabbitmq.consumer

import com.alibaba.fastjson.JSONObject
import com.rabbitmq.client.Channel
import org.apache.commons.lang3.RandomUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class RabbitConsumer2 {

    private val log: Logger = LoggerFactory.getLogger(RabbitConsumer2::class.java)

    @RabbitListener(queues = ["test.queue"])
    fun listener2(@Payload json: JSONObject,
                  message: Message, channel: Channel) {
        val deliveryTag = message.messageProperties.deliveryTag
        log.info("consumer2收到消息：$json，tag: $deliveryTag")
        val localRandom = RandomUtils.nextInt(0, 30)
        if(localRandom < 10) {
            val requeue = localRandom < 5
            log.info("${json["random"]} nack, requeue: $requeue")
            channel.basicNack(deliveryTag, false, requeue)
        } else if(localRandom < 20) {
            log.info("${json["random"]}消费完成")
            channel.basicAck(deliveryTag, false)
        } else {
            val requeue = localRandom < 25
            log.info("${json["random"]} reject, requeue: $requeue")
            channel.basicReject(deliveryTag, requeue)
        }
    }
}