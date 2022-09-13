package de.honoka.test.microservice.order.service

import de.honoka.test.microservice.order.dao.ProductOrderDao
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

@Service
class SystemService(
    val productOrderDao: ProductOrderDao
) {

    @PostConstruct
    fun init() {
        productOrderDao.clear()
    }
}