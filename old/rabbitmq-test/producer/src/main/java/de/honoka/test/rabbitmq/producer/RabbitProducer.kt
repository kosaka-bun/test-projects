@file:Suppress("SpringJavaInjectionPointsAutowiringInspection")

package de.honoka.test.rabbitmq.producer

import com.alibaba.fastjson.JSONObject
import de.honoka.sdk.util.text.TextUtils
import org.apache.commons.lang3.RandomUtils
import org.springframework.amqp.core.AmqpAdmin
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class RabbitProducer(
    val rabbitTemplate: RabbitTemplate,
    val amqpAdmin: AmqpAdmin
) {

    companion object {

        const val EXCHANGE_NAME = "test.exchange"

        const val QUEUE_NAME = "test.queue"
    }

    @GetMapping("/init")
    fun init() {
        val exchange = DirectExchange(EXCHANGE_NAME)
        amqpAdmin.declareExchange(exchange)
        /*
         * 生成一个队列
         * 1.队列名称
         * 2.队列里面的消息是否持久化 默认消息存储在内存中
         * 3.是否只允许创建队列的连接使用这个队列（true if we are declaring an
         * exclusive queue (the queue will only be used by the declarer's)）
         * 4.是否自动删除 最后一个消费者端开连接以后 该队列是否自动删除 true 自动删除
         * 5.其他参数
         */
        val queue = Queue(QUEUE_NAME, false, false,
            false, null)
        amqpAdmin.declareQueue(queue)
        amqpAdmin.declareBinding(Binding(QUEUE_NAME, Binding.DestinationType.QUEUE,
            EXCHANGE_NAME, "test.route", null))
    }

    @PostMapping("/publish")
    fun publish(msg: String,
                @RequestParam(required = false, defaultValue = "2")
                num: Int) {
        for(i in 1..num) {
            publishOne(msg)
        }
    }

    private fun publishOne(msg: String) {
        val json = JSONObject().apply {
            put("timestamp", TextUtils.getSimpleDateFormat().format(Date()))
            put("random", RandomUtils.nextInt(1, 101))
            put("message", msg)
        }
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "test.route", json)
    }
}