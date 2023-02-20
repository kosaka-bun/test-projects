package de.honoka.test.microservice.order.service

import com.alibaba.fastjson.JSONObject
import de.honoka.sdk.util.text.TextUtils
import de.honoka.test.microservice.order.dao.ProductOrderDao
import de.honoka.test.microservice.order.entity.ProductOrder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.context.annotation.Lazy
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service
import java.util.*
import javax.annotation.Resource
import javax.transaction.Transactional

@Service
class ProductOrderService(
    val productOrderDao: ProductOrderDao
) {

    val log: Logger = LoggerFactory.getLogger(ProductOrderService::class.java)

    @Lazy
    @Resource
    lateinit var productOrderService: ProductOrderService

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
        productOrderService.insert(order)
    }

    @Transactional
    fun insert(productOrder: ProductOrder) {
        productOrderDao.insert(productOrder)
    }
}
