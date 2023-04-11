package de.honoka.test.spring.security.security

import de.honoka.test.spring.security.dao.UserDao
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(
    private val userDao: UserDao
) : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails {
        return UserDetailsImpl(userDao.findByUsername(username).let {
            it ?: throw UsernameNotFoundException(username)
        })
    }
}