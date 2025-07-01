package de.honoka.test.spring.security.auth.controller

import cn.hutool.json.JSONObject
import cn.hutool.json.JSONUtil
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import de.honoka.sdk.util.framework.web.ApiResponse
import de.honoka.test.spring.security.auth.entity.User
import de.honoka.test.spring.security.auth.service.UserService
import org.springframework.web.bind.annotation.*

@RequestMapping("/user")
@RestController
class UserController(private val userService: UserService) {

    @PostMapping("/register")
    fun register(@RequestBody user: User): ApiResponse<*> {
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
    fun login(@RequestBody body: JSONObject): ApiResponse<*> = run {
        val res = JSONUtil.parse(userService.login(body))
        ApiResponse.success(res)
    }
}
