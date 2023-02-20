package de.honoka.test.microservice.gateway.feign.feign

import de.honoka.sdk.util.framework.web.ApiResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "microservice-test-product", path = "/product")
//@FeignClient(name = "microservice-test-product", path = "/product",
//    configuration = [ProductFeignClientConfig::class],
//    fallback = ProductFeignClientFallback::class)
interface ProductFeignClient {

    @GetMapping("/count")
    fun count(@RequestParam name: String): ApiResponse<*>

    @PostMapping("/buy")
    fun buy(@RequestParam userId: Int,
            @RequestParam productId: Int): ApiResponse<*>
}

//class ProductFeignClientConfig {
//
//    @Bean
//    fun productFeignClientFallback(): ProductFeignClientFallback {
//        return ProductFeignClientFallback()
//    }
//}
//
//class ProductFeignClientFallback : ProductFeignClient {
//
//    override fun count(name: String): ApiResponse<*> {
//        return ApiResponse.fail("count方法被限流")
//    }
//
//    override fun buy(userId: Int, productId: Int): ApiResponse<*> {
//        return ApiResponse.fail("buy方法被限流")
//    }
//}
