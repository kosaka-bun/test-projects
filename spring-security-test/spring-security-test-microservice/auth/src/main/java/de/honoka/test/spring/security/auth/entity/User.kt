package de.honoka.test.spring.security.auth.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
data class User(

    @Id
    @TableId(type = IdType.ASSIGN_ID)
    var id: Long? = null,

    @Column(unique = true)
    var username: String? = null,

    var password: String? = null
)
