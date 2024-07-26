package de.honoka.test.spring.security.auth.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

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

    private val encoder = BCryptPasswordEncoder()

    override fun getUsername(): String? = user.username

    override fun getPassword(): String? = encoder.encode(user.password)

    override fun getAuthorities(): MutableCollection<out GrantedAuthority>? = null

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}