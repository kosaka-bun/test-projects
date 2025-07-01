package de.honoka.test.spring.security.auth.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

/**
 * 在存在自行保存的登录态的情况下，手动为SecurityContextHolder的context添加authentication信息
 */
object CustomLoginStatusFilter : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        /*
         * 这里必须使用三个参数的UsernamePasswordAuthenticationToken构造方法，因为两个参数的构造方法会
         * 将对象中的authenticated字段设为false，而三个参数的构造方法会设为true。
         */
        val authentication = UsernamePasswordAuthenticationToken(
            "user", null, null
        )
        SecurityContextHolder.getContext().authentication = authentication
        filterChain.doFilter(request, response)
    }
}
