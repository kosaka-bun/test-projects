package de.honoka.test.spring.security.auth.security

import de.honoka.test.spring.security.auth.entity.User
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

    //临时登录态
    val tokenUserMap = HashMap<String, User>()

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val token = request.getHeader("X-Token") ?: request.getParameter("x_token")
        if(token?.isBlank() == true || !tokenUserMap.containsKey(token)) {
            filterChain.doFilter(request, response)
            return
        }
        /*
         * 这里必须使用三个参数的UsernamePasswordAuthenticationToken构造方法，因为两个参数的构造方法会
         * 将对象中的authenticated字段设为false，而三个参数的构造方法会设为true。
         */
        val authentication = UsernamePasswordAuthenticationToken(
            tokenUserMap[token]?.username, null, null
        )
        SecurityContextHolder.getContext().authentication = authentication
        filterChain.doFilter(request, response)
    }
}