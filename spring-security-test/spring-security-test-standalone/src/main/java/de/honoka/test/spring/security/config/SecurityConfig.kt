package de.honoka.test.spring.security.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@EnableWebSecurity
@Configuration
class SecurityConfig {

    companion object {

        val passwordEncoder = BCryptPasswordEncoder()
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain = http.run {
        val whiteList = arrayOf(
            //允许注册
            "/user/**"
        )
        csrf {
            /*
             * 若不在此处对白名单中的URL表达式进行忽略，仅在authorizeHttpRequests中设置permitAll的话，
             * CSRF拦截器会导致白名单中的URL不可由GET以外的方式去访问。
             * 例如，若有一个接口/test被设置为白名单，其同时支持通过GET与POST进行请求，则若不在此处设置
             * 忽略，则仅在通过GET方式请求/test接口时能够正常访问，即使在authorizeHttpRequests中对该接
             * 口设置了白名单，其他方式的请求依旧会被拦截。
             */
            it.ignoringRequestMatchers(*whiteList)
        }
        authorizeHttpRequests {
            it.requestMatchers(*whiteList).permitAll()
            //设置其他所有请求都需要认证
            it.anyRequest().authenticated()
        }
        formLogin(Customizer.withDefaults())
        build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = passwordEncoder
}