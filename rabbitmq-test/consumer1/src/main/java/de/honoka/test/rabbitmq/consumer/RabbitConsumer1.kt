package de.honoka.test.rabbitmq.consumer

import com.alibaba.fastjson.JSONObject
import com.rabbitmq.client.Channel
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class RabbitConsumer1 {

    private val log: Logger = LoggerFactory.getLogger(RabbitConsumer1::class.java)

    private var count: ThreadLocal<Int> = ThreadLocal()

    @RabbitListener(queues = ["test.queue"])
    fun listener1(@Payload json: JSONObject,
                  message: Message, channel: Channel) {
        val deliveryTag = message.messageProperties.deliveryTag
        log.info("consumer1收到消息：$json，tag: $deliveryTag")
        Thread.sleep(5 * 1000)
        log.info("${json["random"]}消费完成")
        //计数
        count.get() ?: count.set(0)
        count.set(count.get() + 1)
        if(count.get() < 3) return
        /*
         * multiple：当设为true时，当前线程会批量确认那些deliveryTag小于所传入的
         * deliveryTag参数的消息
         * 譬如，一个线程一次拉取了3条消息，tag分别是1 2 3
         * 当tag为3的消息消费完成后，仅需在ack时提供3这个tag，指定multiple为true
         * 即可将1 2 3均设为ack
         */
        channel.basicAck(deliveryTag, true)
        log.info("$deliveryTag has acked")
        count.set(0)
    }
}