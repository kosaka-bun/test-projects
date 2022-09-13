package de.honoka.test.microservice.product.controller

import com.alibaba.fastjson.JSONObject
import de.honoka.sdk.util.web.ApiResponse
import de.honoka.test.microservice.product.dao.ProductDao
import de.honoka.test.microservice.product.util.RedisUtils
import org.redisson.api.RLock
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.TimeUnit

@RequestMapping("/product")
@RestController
class ProductController(
    val productDao: ProductDao,
    val rabbitTemplate: RabbitTemplate,
    val redisUtils: RedisUtils
) {

    val log: Logger = LoggerFactory.getLogger(ProductController::class.java)

    @GetMapping("/count")
    fun count(name: String): ApiResponse<*> {
        val product = productDao.findByName(name).run {
            if(isEmpty()) null else get(0)
        }
        return ApiResponse.success(JSONObject().apply {
            put("id", product?.id)
            put("count", product?.count)
        })
    }

    @PostMapping("/buy")
    fun buy(userId: Int, productId: Int): ApiResponse<*> {
        //预减库存
        val lock: RLock = redisUtils.getLock("$productId:lock")
        try {
            if(!lock.tryLock(5, TimeUnit.SECONDS))
                return ApiResponse.fail("wait lock timeout")
            val count: Int = if(redisUtils.hasKey("$productId:count")) {
                Integer.parseInt(redisUtils.get("$productId:count"))
            } else {
                val queryCount = productDao.selectById(productId).count
                queryCount!!
            }
            if(count <= 0)
                return ApiResponse.fail("sold out")
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
        return ApiResponse.success(null)
    }
}