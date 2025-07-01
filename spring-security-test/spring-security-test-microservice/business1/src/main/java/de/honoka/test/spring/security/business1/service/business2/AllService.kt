package de.honoka.test.spring.security.business1.service.business2

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping

@FeignClient(name = "spring-security-test-business2", path = "/")
interface AllService {

    @GetMapping("/res1")
    fun res1(): String
}
