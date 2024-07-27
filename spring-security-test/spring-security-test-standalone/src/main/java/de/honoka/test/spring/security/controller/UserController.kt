package de.honoka.test.spring.security.controller

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import de.honoka.sdk.util.framework.web.ApiResponse
import de.honoka.test.spring.security.entity.User
import de.honoka.test.spring.security.service.UserService
import org.springframework.web.bind.annotation.*

@RequestMapping("/user")
@RestController
class UserController(private val userService: UserService) {

    @PostMapping
    fun add(@RequestBody user: User): ApiResponse<*> {
        userService.save(user)
        return ApiResponse.success()
    }

    @GetMapping("/{name}")
    fun findByName(@PathVariable name: String): ApiResponse<*> {
        val user = userService.getOne(QueryWrapper<User>().run {
            eq("username", name)
        })
        return ApiResponse.success(user)
    }
}