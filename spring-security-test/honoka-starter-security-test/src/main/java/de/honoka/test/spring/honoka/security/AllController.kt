package de.honoka.test.spring.honoka.security

import de.honoka.sdk.spring.starter.security.DefaultUser
import de.honoka.sdk.spring.starter.security.token.JwtUtils
import de.honoka.sdk.util.web.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AllController {
    
    @GetMapping("/")
    fun index(): ApiResponse<*> = ApiResponse.success("hello")
    
    @PostMapping("/login")
    fun login(): ApiResponse<*> {
        val user = DefaultUser().apply {
            id = 1
        }
        return ApiResponse.success(JwtUtils.newJwt(user))
    }
    
    @PostMapping("/logout")
    fun logout(): ApiResponse<*> {
        JwtUtils.cancelJwt()
        return ApiResponse.success()
    }
}