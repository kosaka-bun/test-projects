package de.honoka.test.microservice.product.util

import de.honoka.test.microservice.product.ProductApplication
import de.honoka.test.microservice.product.service.ProductService
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

@Component
class ComponentHolder {

    val applicationContext: ApplicationContext by lazy {
        ProductApplication.applicationContext
    }

    fun <T> getBean(clazz: Class<T>): T {
        return applicationContext.getBean(clazz)
    }

    val productService: ProductService by lazy {
        getBean(ProductService::class.java)
    }
}