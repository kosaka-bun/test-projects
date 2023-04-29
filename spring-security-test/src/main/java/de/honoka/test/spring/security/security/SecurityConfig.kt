package de.honoka.test.spring.security.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import kotlin.apply as kotlinApply

@EnableWebSecurity
@Configuration
class SecurityConfig(
    private val loginFilter: LoginFilter,
    private val authenticationEntryPoint: AuthenticationEntryPoint,
    private val accessDeniedHandler: AccessDeniedHandler
) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http.kotlinApply {
            //关闭csrf
            csrf().disable()
            //不通过Session获取SecurityContext
            sessionManagement().sessionCreationPolicy(
                SessionCreationPolicy.STATELESS)
            //配置请求认证逻辑
            authorizeRequests().kotlinApply {
                //允许注册
                antMatchers(HttpMethod.POST, "/user").permitAll()
                //对于登录接口，允许匿名访问，不允许在有token的情况下访问
                /*
                 * 即，在到达这个接口前，若SpringContextHolder的context中有
                 * authentication，则抛出AccessDeniedException，禁止这一请求
                 */
                antMatchers("/user/login").anonymous()
                //除上面外的所有请求全部需要鉴权认证
                anyRequest().authenticated()
            }
            //添加过滤器
            addFilterBefore(
                loginFilter,
                UsernamePasswordAuthenticationFilter::class.java
            )
            //配置异常处理器
            exceptionHandling().kotlinApply {
                authenticationEntryPoint(authenticationEntryPoint)
                accessDeniedHandler(accessDeniedHandler)
            }
        }
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager =
        super.authenticationManagerBean()
}