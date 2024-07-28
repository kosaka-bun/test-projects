package de.honoka.test.spring.security.auth.controller

import cn.hutool.json.JSONObject
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import de.honoka.sdk.util.framework.web.ApiResponse
import de.honoka.test.spring.security.auth.entity.User
import de.honoka.test.spring.security.auth.security.CustomLoginStatusFilter
import de.honoka.test.spring.security.auth.service.UserService
import org.springframework.web.bind.annotation.*
import java.util.*

@RequestMapping("/user")
@RestController
class UserController(private val userService: UserService) {

    @PostMapping("/info")
    fun add(@RequestBody user: User): ApiResponse<*> {
        userService.save(user)
        return ApiResponse.success()
    }

    @GetMapping("/info/{name}")
    fun findByName(@PathVariable name: String): ApiResponse<*> {
        val user = userService.getOne(QueryWrapper<User>().run {
            eq("username", name)
        })
        return ApiResponse.success(user)
    }

    @PostMapping("/login")
    fun login(@RequestBody body: JSONObject): ApiResponse<*> {
        val token = UUID.randomUUID().toString()
        val user = userService.baseMapper.findByUsername(body["username"] as String)
        user ?: throw RuntimeException("用户不存在")
        CustomLoginStatusFilter.tokenUserMap[token] = user
        return ApiResponse.success(JSONObject().set("token", token))
    }
}