package de.honoka.test.microservice.product.service

import com.alibaba.fastjson.JSONObject
import de.honoka.test.microservice.product.dao.ProductDao
import de.honoka.test.microservice.product.util.RedisUtils
import org.redisson.api.RLock
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.context.annotation.Lazy
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit
import javax.annotation.Resource
import javax.transaction.Transactional

@Service
class ProductService(
    val productDao: ProductDao,
    val rabbitTemplate: RabbitTemplate,
    val redisUtils: RedisUtils
) {

    val log: Logger = LoggerFactory.getLogger(ProductService::class.java)

    @Lazy
    @Resource
    lateinit var productService: ProductService

    fun count(name: String): JSONObject {
        val product = productDao.findByName(name).run {
            if(isEmpty()) null else get(0)
        }
        return JSONObject().apply {
            put("id", product?.id)
            put("count", product?.count)
        }
    }

    fun buy(userId: Int, productId: Int) {
        //预减库存
        val lock: RLock = redisUtils.getLock("$productId:lock")
        try {
            if(!lock.tryLock(5, TimeUnit.SECONDS))
                throw RuntimeException("wait lock timeout")
            val count: Int = if(redisUtils.hasKey("$productId:count")) {
                Integer.parseInt(redisUtils.get("$productId:count"))
            } else {
                val queryCount = productDao.selectById(productId).count
                queryCount!!
            }
            if(count <= 0)
                throw RuntimeException("sold out")
            redisUtils.set("$productId:count", count - 1,
                    10, TimeUnit.SECONDS)
            rabbitTemplate.convertAndSend("product.decreaseCountQueue",
                    JSONObject().apply {
                        put("product_id", productId)
                        put("count", count - 1)
                    })
        } finally {
            if(lock.isHeldByCurrentThread) lock.unlock()
        }
        //发消息下单
        rabbitTemplate.convertAndSend("order.queue",
                JSONObject().apply {
                    put("user_id", userId)
                    put("product_id", productId)
                })
        log.info("user_id=$userId, product_id=$productId, count=${redisUtils
                .get("$productId:count")}")
    }

    @RabbitListener(queues = ["product.decreaseCountQueue"])
    fun applyDecreaseCountMsg(@Payload json: JSONObject) {
        productService.decreaseCount(
            json.getInteger("product_id"),
            json.getInteger("count")
        )
    }

    @Transactional
    fun decreaseCount(productId: Int, count: Int) {
        val product = productDao.selectForUpdate(productId)
        if(count < product.count!!)
            productDao.setCount(productId, count)
    }
}
