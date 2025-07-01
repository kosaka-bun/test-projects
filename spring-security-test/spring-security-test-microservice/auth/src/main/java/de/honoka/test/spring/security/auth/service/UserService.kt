package de.honoka.test.spring.security.auth.service

import cn.hutool.http.HttpUtil
import cn.hutool.json.JSONObject
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import de.honoka.test.spring.security.auth.entity.User
import de.honoka.test.spring.security.auth.mapper.UserMapper
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.net.URI

@Service
class UserService(private val userMapper: UserMapper) : ServiceImpl<UserMapper, User>() {

    @Value("\${server.port}")
    private var serverPort: Int = 0

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

    fun login(params: JSONObject): String {
        val user = baseMapper.findByUsername(params.getStr("username"))
        if(user == null || user.password != params.getStr("password")) {
            throw RuntimeException("用户名或密码错误")
        }
        var url = "http://localhost:$serverPort/oauth2/authorize?response_type=code&client_id=oauth2-client1&scope=all" +
            "&redirect_uri=https%3A%2F%2Fwww.baidu.com"
        var res = HttpUtil.createGet(url).run {
            setFollowRedirects(false)
            URI.create(execute().header("Location")).rawQuery
        }
        if(res.contains("state=")) {
            url = "http://localhost:$serverPort/oauth2/authorize"
            res = HttpUtil.createPost(url).run {
                setFollowRedirects(false)
                body(res)
                URI.create(execute().header("Location")).rawQuery
            }
        }
        url = "http://localhost:$serverPort/oauth2/token"
        res = HttpUtil.createPost(url).run {
            /*
             * Authorization的值为SecurityConfig中所配置的client的clientId与clientSecret（原文），
             * 用冒号拼接，然后base64编码
             */
            auth("Basic b2F1dGgyLWNsaWVudDE6MTIzNDU2")
            body("$res&grant_type=authorization_code&redirect_uri=https%3A%2F%2Fwww.baidu.com")
            execute().body()
        }
        return res
    }
}
