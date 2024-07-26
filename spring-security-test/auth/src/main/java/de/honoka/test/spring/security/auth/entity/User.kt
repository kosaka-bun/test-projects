package de.honoka.test.spring.security.auth.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import de.honoka.test.spring.security.auth.config.SecurityConfig
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
data class User(

    @Id
    @TableId(type = IdType.ASSIGN_ID)
    var id: Long? = null,

    @Column(unique = true)
    var username: String? = null,

    var password: String? = null
)

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