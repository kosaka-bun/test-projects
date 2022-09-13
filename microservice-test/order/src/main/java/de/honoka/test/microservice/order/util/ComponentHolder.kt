package de.honoka.test.microservice.order.util

import de.honoka.test.microservice.order.OrderApplication
import de.honoka.test.microservice.order.service.ProductOrderService
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

@Component
class ComponentHolder {

    val applicationContext: ApplicationContext by lazy {
        OrderApplication.applicationContext
    }

    fun <T> getBean(clazz: Class<T>): T {
        return applicationContext.getBean(clazz)
    }

    val productOrderService by lazy {
        getBean(ProductOrderService::class.java)
    }
}