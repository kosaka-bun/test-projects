package de.honoka.test.spring.security.auth.service

import de.honoka.test.spring.security.auth.entity.User
import de.honoka.test.spring.security.auth.entity.UserDetailsImpl
import de.honoka.test.spring.security.auth.mapper.UserMapper
import jakarta.annotation.PostConstruct
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(private val userMapper: UserMapper) : UserDetailsService {

    @PostConstruct
    fun init() {
        userMapper.findByUsername("admin") ?: run {
            val admin = User().apply {
                username = "admin"
                password = "123456"
            }
            userMapper.insert(admin)
        }
    }

    override fun loadUserByUsername(username: String?): UserDetails = run {
        val user = userMapper.findByUsername(username) ?: throw UsernameNotFoundException(username)
        UserDetailsImpl(user)
    }
}