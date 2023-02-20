package de.honoka.test.microservice.gateway.feign.service

import com.alibaba.csp.sentinel.annotation.SentinelResource
import com.alibaba.csp.sentinel.slots.block.BlockException
import de.honoka.sdk.util.framework.web.ApiResponse
import de.honoka.test.microservice.gateway.feign.feign.ProductFeignClient
import org.springframework.stereotype.Service

@Service
class ProductService(
    val productFeignClient: ProductFeignClient
) {

    fun count(name: String): ApiResponse<*> {
        return productFeignClient.count(name)
    }

    @SentinelResource("ProductService.buy", blockHandler = "onBuyBlocked",
        blockHandlerClass = [ProductServiceBlockHandler::class])
    fun buy(userId: Int, productId: Int): ApiResponse<*> {
        return productFeignClient.buy(userId, productId)
    }
}

class ProductServiceBlockHandler {

    companion object {

        @JvmStatic
        fun onBuyBlocked(userId: Int, productId: Int,
                         blockException: BlockException): ApiResponse<*> {
            return ApiResponse.fail("blocked")
        }
    }
}
