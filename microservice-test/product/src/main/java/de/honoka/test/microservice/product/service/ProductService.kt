package de.honoka.test.microservice.product.service

import com.alibaba.fastjson.JSONObject
import de.honoka.test.microservice.product.dao.ProductDao
import de.honoka.test.microservice.product.util.ComponentHolder
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service
import javax.annotation.Resource
import javax.transaction.Transactional

@Service
class ProductService(
    val productDao: ProductDao
) {

    @Resource
    lateinit var componentHolder: ComponentHolder

    @RabbitListener(queues = ["product.decreaseCountQueue"])
    fun applyDecreaseCountMsg(@Payload json: JSONObject) {
        componentHolder.productService.decreaseCount(
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