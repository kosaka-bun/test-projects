package de.honoka.test.microservice.gateway.feign.controller

import de.honoka.sdk.util.web.ApiResponse
import de.honoka.test.microservice.gateway.feign.service.ProductService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/product")
@RestController
class ProductController(
    val productService: ProductService
) {

    @GetMapping("/count")
    fun count(name: String): ApiResponse<*> {
        return productService.count(name)
    }

    @PostMapping("/buy")
    fun buy(userId: Int, productId: Int): ApiResponse<*>? {
        return productService.buy(userId, productId)
    }
}