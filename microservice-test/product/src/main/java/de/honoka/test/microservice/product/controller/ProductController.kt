package de.honoka.test.microservice.product.controller

import de.honoka.sdk.util.framework.web.ApiResponse
import de.honoka.test.microservice.product.service.ProductService
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
        return ApiResponse.success(productService.count(name))
    }

    @PostMapping("/buy")
    fun buy(userId: Int, productId: Int): ApiResponse<*> {
        productService.buy(userId, productId)
        return ApiResponse.success(null)
    }
}
