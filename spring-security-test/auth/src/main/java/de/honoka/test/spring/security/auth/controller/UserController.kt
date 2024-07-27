package de.honoka.test.spring.security.auth.controller

import de.honoka.sdk.util.framework.web.ApiResponse
import de.honoka.test.spring.security.auth.entity.User
import de.honoka.test.spring.security.auth.service.UserService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/user")
@RestController
class UserController(private val userService: UserService) {

    @PostMapping
    fun add(@RequestBody user: User): ApiResponse<*> {
        userService.save(user)
        return ApiResponse.success()
    }
}