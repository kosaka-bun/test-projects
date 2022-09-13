package de.honoka.test.microservice.order.service

import com.alibaba.fastjson.JSONObject
import de.honoka.sdk.util.text.TextUtils
import de.honoka.test.microservice.order.dao.ProductOrderDao
import de.honoka.test.microservice.order.entity.ProductOrder
import de.honoka.test.microservice.order.util.ComponentHolder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service
import java.util.*
import javax.transaction.Transactional

@Service
class ProductOrderService(
    val productOrderDao: ProductOrderDao,
    val componentHolder: ComponentHolder
) {

    val log: Logger = LoggerFactory.getLogger(ProductOrderService::class.java)

    @RabbitListener(queues = ["order.queue"])
    fun createOrder(@Payload json: JSONObject) {
        val order = ProductOrder().apply {
            userId = json.getInteger("user_id")
            productId = json.getInteger("product_id")
            orderTime = Date()
        }
        log.info("order: user_id=${order.userId}, product_id=${order.productId}, " +
                "order_time=${TextUtils.getSimpleDateFormat().format(
                    order.orderTime)}")
        componentHolder.productOrderService.insert(order)
    }

    @Transactional
    fun insert(productOrder: ProductOrder) {
        productOrderDao.insert(productOrder)
    }
}