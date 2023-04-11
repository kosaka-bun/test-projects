package de.honoka.test.spring.security.controller

import de.honoka.sdk.json.api.JsonObject
import de.honoka.test.spring.security.dao.UserDao
import de.honoka.test.spring.security.entity.User
import de.honoka.test.spring.security.security.LoginCache
import de.honoka.test.spring.security.security.UserDetailsImpl
import de.honoka.test.spring.security.util.Utils
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*
import javax.servlet.http.HttpServletRequest

@RequestMapping("/user")
@RestController
class UserController(
    private val userDao: UserDao,
    private val authenticationManager: AuthenticationManager
) {

    @GetMapping("/all")
    fun getAll(): List<User> {
        return userDao.selectList(null)
    }

    @PostMapping
    fun insertOne() {
        val random = Random()
        userDao.insert(User(
            null,
            "user-${random.nextInt(1000) + 1}",
            "abc123456"
        ))
    }

    @PostMapping("/login")
    fun login(username: String?, password: String?): JsonObject {
        //使用Authentication的实现类
        //手动调用方法去认证，会自动调用UserDetailsService查，然后对比
        val authentication: Authentication? = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(username, password)
        )
        authentication ?: return JsonObject.of().add("msg", "用户名或密码错误")
        val userDetails = authentication.principal as UserDetailsImpl
        return JsonObject.of().add("token", UUID.randomUUID().toString().also {
            LoginCache.tokenUserMap[it] = userDetails.user
        })
    }

    @PostMapping("/logout")
    fun logout(request: HttpServletRequest) {
        val token = Utils.getTokenByAuthorization(
            request.getHeader("Authorization")
        )
        LoginCache.tokenUserMap.remove(token)
    }
}