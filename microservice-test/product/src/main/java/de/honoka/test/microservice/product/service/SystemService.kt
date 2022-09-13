package de.honoka.test.microservice.product.service

import de.honoka.test.microservice.product.dao.ProductDao
import de.honoka.test.microservice.product.entity.Product
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct
import javax.transaction.Transactional

@Service
class SystemService(
    val productDao: ProductDao
) {

    @PostConstruct
    @Transactional
    fun init() {
        val product = productDao.selectById(1)
        if(product == null) {
            productDao.clear()
            productDao.insert(Product().apply {
                id = 1
                name = "TestProduct"
                count = 100
            })
        } else {
            product.count = 100
            productDao.updateById(product)
        }
    }
}