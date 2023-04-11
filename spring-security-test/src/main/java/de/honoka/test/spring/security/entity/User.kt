package de.honoka.test.spring.security.entity

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import javax.persistence.*

@Entity
data class User(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(type = IdType.AUTO)
    var id: Int? = null,

    @Column(unique = true)
    var username: String? = null,

    var password: String? = null
)