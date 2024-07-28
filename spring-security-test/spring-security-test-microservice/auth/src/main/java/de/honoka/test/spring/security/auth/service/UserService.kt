package de.honoka.test.spring.security.auth.service

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import de.honoka.test.spring.security.auth.entity.User
import de.honoka.test.spring.security.auth.mapper.UserMapper
import de.honoka.test.spring.security.auth.security.SecurityConfig
import jakarta.annotation.PostConstruct
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserService(private val userMapper: UserMapper) : ServiceImpl<UserMapper, User>(), UserDetailsService {

    class UserDetailsImpl(private val user: User) : UserDetails {

        override fun getUsername(): String? = user.username

        override fun getPassword(): String? = SecurityConfig.passwordEncoder.encode(user.password)

        override fun getAuthorities(): MutableCollection<out GrantedAuthority> = arrayListOf(
            SimpleGrantedAuthority("USER")
        )

        override fun isAccountNonExpired(): Boolean = true

        override fun isAccountNonLocked(): Boolean = true

        override fun isCredentialsNonExpired(): Boolean = true

        override fun isEnabled(): Boolean = true
    }

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