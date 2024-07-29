package de.honoka.test.spring.security.business1.controller

import de.honoka.sdk.util.framework.web.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AllController {

    @GetMapping("/res1")
    fun res1(): ApiResponse<*> = ApiResponse.success("res1")
}