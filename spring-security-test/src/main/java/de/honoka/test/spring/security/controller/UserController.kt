package de.honoka.test.spring.security.controller

import de.honoka.test.spring.security.dao.UserDao
import de.honoka.test.spring.security.entity.User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RequestMapping("/user")
@RestController
class UserController(
    private val userDao: UserDao
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
}