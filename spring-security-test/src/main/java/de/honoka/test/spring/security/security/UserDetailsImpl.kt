package de.honoka.test.spring.security.security

import de.honoka.test.spring.security.entity.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class UserDetailsImpl(
    private val user: User
) : UserDetails {

    private val encoder = BCryptPasswordEncoder()

    override fun getUsername(): String? = user.username

    override fun getPassword(): String? = encoder.encode(user.password)

    override fun getAuthorities(): MutableCollection<out GrantedAuthority>? = null

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}