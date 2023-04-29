package de.honoka.test.spring.security.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class User(

    @Id
    @TableId(type = IdType.ASSIGN_ID)
    var id: Int? = null,

    @Column(unique = true)
    var username: String? = null,

    var password: String? = null
)